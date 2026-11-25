package com.beyond.bycontract.subscription.infrastructure.adapter;

import com.beyond.bycontract.subscription.application.port.PaymentGateway;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StripePaymentGatewayAdapter implements PaymentGateway {
    @Override
    public String createCheckoutSession(UUID idUser, String email, String stripeIdCustomer, String idPrice, String successUrl, String cancelUrl) {
        try {
            SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                    // Mode Abonnement
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setClientReferenceId(idUser.toString())
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setPrice(idPrice)
                            .setQuantity(1L)
                            .build())
                    // IMPORTANT : Permet de retrouver l'utilisateur dans le Webhook
                    .putMetadata("idUser", idUser.toString());


            if (stripeIdCustomer != null && !stripeIdCustomer.isBlank()) {
                paramsBuilder.setCustomer(stripeIdCustomer);
            } else {
                paramsBuilder.setCustomerEmail(email);
            }

            Session session = Session.create(paramsBuilder.build());
            return session.getUrl();
        }catch (Exception e) {
            System.err.println("❌ CAUSE RÉELLE STRIPE : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la création de la session Stripe", e);
        }
    }


}
