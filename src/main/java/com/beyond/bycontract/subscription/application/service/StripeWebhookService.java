package com.beyond.bycontract.subscription.application.service;

import com.beyond.bycontract.email.domain.repository.EmailRepository;
import com.beyond.bycontract.user.domain.exception.UserNotFoundException;
import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.domain.repository.UserRepository;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookService {

    @Value("${application.cors.allowed-origins}")
    private String frontendUrlOrigin;
    private String frontendUrl = frontendUrlOrigin + "/billing";

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;

    @Transactional
    public void processWebhookEvent(Event event) {
        log.info("Webhook stripe received: {}", event.getType());
        switch (event.getType()) {
            case "checkout.session.completed":
                //Initial Subscription created
                handleCheckoutSessionCompleted(event);
                break;
            case "customer.subscription.trial_will_end":
                //J-3 before the end of trial
                handleTrialWillEnd(event);
                break;
            case "customer.subscription.updated":
                //Status changing (e.g: active -> past_due if card is blocked)
                handleSubscriptionUpdated(event);
                break;
            case "customer.updated":
                handleCustomerUpdated(event);
                break;
            case "customer.subscription.deleted":
                //L'utilisateur a annulé son abonnement ou le délai de grâce est dépassé
                handleSubscriptionDeleted(event);
                break;
            case "invoice.payment_failed":
                // 5. Un paiement de renouvellement a échoué
                handleInvoicePaymentFailed(event);
                break;
            default:
                log.info("Événement non traité : {}", event.getType());
        }
    }

    private void updatePlanDetailsFromSubscription(Subscription subscription, User user) {
        try {

            Long periodEnd = null;
            String planName = null;
            Double planPrice = null;

            if(subscription.getItems()!= null && !subscription.getItems().getData().isEmpty()) {
                SubscriptionItem item = subscription.getItems().getData().get(0);
                periodEnd = item.getCurrentPeriodEnd();
                Price price = item.getPrice();
                if(price != null && price.getCurrency() != null) {
                    // 2. Conversion des centimes en euros (ex: 2490 -> 24.90)
                    planPrice = price.getUnitAmount() / 100.0;
                    Product product = Product.retrieve(price.getProduct());
                    planName = product.getName();
                }
            }

            if(planName != null && planPrice != null) {
                user.setPlanData(planName, planPrice, periodEnd);
            } else {
                user.updatePeriodEnd(periodEnd);
            }



        }catch (Exception e){
            log.error("Impossible de récupérer les détails du plan pour la souscription {}", subscription.getId(), e);
        }
    }

    @Transactional
    public void handleCheckoutSessionCompleted(Event event) {
        log.info("🔴 Événement checkout.session.completed intercepté !");

        Session session;
        try {
            session = (Session) event.getDataObjectDeserializer().deserializeUnsafe();
        } catch (EventDataObjectDeserializationException e) {
            log.error("❌ Échec de la lecture de l'objet Stripe", e);
            return;
        }

        // 1. Vérification sécurisée des metadata
        String idUserStr = session.getMetadata() != null ? session.getMetadata().get("idUser") : null;
        if (idUserStr == null) {
            log.warn("Aucun idUser trouvé dans les metadata. Session: {}", session.getId());
            return;
        }

        UUID idUser = UUID.fromString(idUserStr);
        String idCustomer = session.getCustomer();
        String idSubscription = session.getSubscription();
        Subscription subscription = null;
        String brand = null;
        String last4 = null;

        // 2. Appels API Stripe (avant de verrouiller la ligne en base)
        if (idSubscription != null) {
            try {
                subscription = Subscription.retrieve(idSubscription);
                String idPaymentMethod = subscription.getDefaultPaymentMethod();

                if (idPaymentMethod == null && idCustomer != null) {
                    Customer customer = Customer.retrieve(idCustomer);
                    if (customer.getInvoiceSettings() != null) {
                        idPaymentMethod = customer.getInvoiceSettings().getDefaultPaymentMethod();
                    }
                }

                if (idPaymentMethod != null) {
                    PaymentMethod paymentMethod = PaymentMethod.retrieve(idPaymentMethod);
                    if (paymentMethod.getCard() != null) {
                        brand = paymentMethod.getCard().getBrand();
                        last4 = paymentMethod.getCard().getLast4();
                    }
                }
            } catch (StripeException e) {
                log.error("Impossible de récupérer la souscription {} : {}", idSubscription, e.getMessage());
            }
        }

        // 3. Verrouillage pessimiste et mise à jour atomique
        User user = userRepository.getUserByIdWithLock(idUser)
                .orElseThrow(() -> new UserNotFoundException(idUser));

        user.activateSubscription(idCustomer, idSubscription);

        if (brand != null && last4 != null) {
            user.setCardData(brand, last4);
        }
        if (subscription != null) {
            updatePlanDetailsFromSubscription(subscription, user);
        }

        userRepository.updateUser(user);
        log.info("✅ Abonnement activé pour l'utilisateur {}. SubscriptionID: {}", idUser, idSubscription);
    }

    // --- 1. J-3 AVANT LA FIN DE L'ESSAI ---
    private void handleTrialWillEnd(Event event)  {
        Subscription subscription;
        try {
            subscription = (Subscription) event.getDataObjectDeserializer().deserializeUnsafe();
        } catch (Exception e) {
            log.error("Echec de la lecture de l'objet Subscription", e);
            return;
        }

        String idSubscription = subscription.getId();
        userRepository.findByStripeIdSubscription(idSubscription).ifPresentOrElse(user -> {
            log.info("⚠️ Fin d'essai imminente (J-3) pour l'utilisateur : {}", user.getId());
            // TODO : Mettre à jour l'utilisateur (ex: user.setTrialEndingSoon(true))
            emailRepository.sendTrialEndingEmail(user.getEmail(), user.getFirstName(), frontendUrl);
        },
                () -> log.error("Webhook customer.subscription.trial_will_end reçu pour une souscription Stripe inconnu: {}", idSubscription));
    }

    // --- 2. MISE À JOUR DE L'ABONNEMENT (Renouvellement, impayé...) ---
    @Transactional
    public void handleSubscriptionUpdated(Event event) {
        Subscription subscription;
        try {
            subscription = (Subscription) event.getDataObjectDeserializer().deserializeUnsafe();
        } catch (Exception e) {
            log.error("Échec de la lecture de l'objet Subscription", e);
            return;
        }

        String stripeIdCustomer = subscription.getCustomer();
        String stripeIdSubscription = subscription.getId();

        // 1. Appels réseau Stripe AVANT de verrouiller la ligne en base
        String brand = null;
        String last4 = null;
        boolean paymentMethodChecked = false;

        String idPaymentMethod = subscription.getDefaultPaymentMethod();
        try {
            if (idPaymentMethod == null && stripeIdCustomer != null) {
                Customer customer = Customer.retrieve(stripeIdCustomer);
                if (customer.getInvoiceSettings() != null) {
                    idPaymentMethod = customer.getInvoiceSettings().getDefaultPaymentMethod();
                }
            }

            if (idPaymentMethod != null) {
                PaymentMethod paymentMethod = PaymentMethod.retrieve(idPaymentMethod);
                if (paymentMethod.getCard() != null) {
                    brand = paymentMethod.getCard().getBrand();
                    last4 = paymentMethod.getCard().getLast4();
                }
            }
            paymentMethodChecked = true;
        } catch (StripeException e) {
            log.error("Impossible de récupérer la méthode de paiement pour le client: {}", stripeIdCustomer, e);
        }

        // 2. Verrouillage pessimiste avec le BON paramètre (stripeIdCustomer)
        Optional<User> userOpt = userRepository.findByStripeIdCustomerForUpdate(stripeIdCustomer);
        if (userOpt.isEmpty()) {
            log.warn("Aucun utilisateur trouvé pour le client {} (souscription {})", stripeIdCustomer, stripeIdSubscription);
            return;
        }

        User user = userOpt.get();

        if (!stripeIdSubscription.equals(user.getStripeIdSubscription())) {
            user.updateIdSubscription(stripeIdSubscription);
        }

        // 3. Mise à jour du statut
        if ("active".equals(subscription.getStatus()) && Boolean.TRUE.equals(subscription.getCancelAtPeriodEnd())) {
            user.updateSubscriptionStatus("cancel_at_period_end");
        } else {
            user.updateSubscriptionStatus(subscription.getStatus());
        }

        // 4. Mise à jour de la carte (uniquement si l'appel Stripe n'a pas échoué)
        if (paymentMethodChecked) {
            if (brand != null && last4 != null) {
                user.setCardData(brand, last4);
                log.info("Carte mise à jour pour l'utilisateur {} : {} ending in {}", user.getId(), brand, last4);
            } else {
                log.warn("Aucune méthode de paiement trouvée pour la souscription {}", stripeIdSubscription);
                user.removeCardData();
            }
        }

        updatePlanDetailsFromSubscription(subscription, user);
        userRepository.updateUser(user);
        log.info("Mise à jour de l'abonnement terminée pour l'utilisateur {}", user.getId());
    }

    // --- 3. ABONNEMENT ANNULÉ ---
    @Transactional
    public void handleSubscriptionDeleted(Event event)  {
        Subscription subscription;
        try {
            subscription = (Subscription) event.getDataObjectDeserializer().deserializeUnsafe();
        }catch (Exception e) {
            log.error("Echec de la lecture de l'objet Subscription", e);
            return;
        }

        userRepository.findByStripeIdSubscription(subscription.getId()).ifPresentOrElse(user -> {
            log.warn("Abonnement annule pour l'utilisateur: {}", user.getId());
            user.cancelSubscription();
            // Optionnel : Retirer les droits premium ici
            userRepository.updateUser(user);
            emailRepository.sendSubscriptionCanceledEmail(user.getEmail(), user.getFirstName(), frontendUrl);
        },
                () -> log.error("Webhook customer.subscription.deleted reçu pour une souscription Stripe inconnu: {}", subscription.getId()));
    }

    // --- 4. ÉCHEC DE PAIEMENT ---
    @Transactional
    public void handleInvoicePaymentFailed(Event event)  {
        Invoice invoice;
        try {
            invoice = (Invoice) event.getDataObjectDeserializer().deserializeUnsafe();
        }catch (Exception e) {
            log.error("Echec de la lecture de l'objet invoice",e);
            return ;
        }

        String idCustomer = invoice.getCustomer();
        userRepository.findByStripeIdCustomer(idCustomer).ifPresentOrElse(user -> {
            log.warn("Echec de paiement pour l'utilisateur: {}", user.getId());
            user.markPaymentFailed();
            userRepository.updateUser(user);
            emailRepository.sendPaymentFailedEmail(user.getEmail(), user.getFirstName(), invoice.getHostedInvoiceUrl());
        },
                () -> log.error("Webhook invoice.payment_failed reçu pour un client Stripe inconnu: {}", idCustomer));
    }

    @Transactional
    public void handleCustomerUpdated(Event event) {
        Customer customer;
        try {
            customer = (Customer) event.getDataObjectDeserializer().deserializeUnsafe();
        }catch (EventDataObjectDeserializationException e) {
            log.error("Echec de la lecture de l'objet customer",e);
            return ;
        }

        String stripeIdCustomer = customer.getId();

        userRepository.findByStripeIdCustomerForUpdate(stripeIdCustomer).ifPresentOrElse(user -> {
            try {
                String idPaymentMethod = null;

                if(customer.getInvoiceSettings() != null) {
                    idPaymentMethod = customer.getInvoiceSettings().getDefaultPaymentMethod();
                }

                if(idPaymentMethod != null) {
                    PaymentMethod paymentMethod = PaymentMethod.retrieve(idPaymentMethod);
                    if(paymentMethod.getCard() != null) {
                        String brand = paymentMethod.getCard().getBrand();
                        String last4 = paymentMethod.getCard().getLast4();
                        user.setCardData(brand, last4);
                        log.info("Carte mise a jour via customer.updated pour l'utilisater {} : {} ending in {}", user.getId(), brand, last4);
                    }
                } else {
                    log.warn("Aucune methode de paiement par defaur sur le client {}. Nettoyage en base", stripeIdCustomer);
                    user.removeCardData();
                }

                userRepository.updateUser(user);
            }catch (StripeException e) {
                log.error("Impossible de récupérer la méthode de paiement pour le client: {}", stripeIdCustomer, e);
            }
        },
                () -> log.warn(" Webhook customer.updated reçu pour un client Stripe inconnu: {}", stripeIdCustomer));;
    }
}
