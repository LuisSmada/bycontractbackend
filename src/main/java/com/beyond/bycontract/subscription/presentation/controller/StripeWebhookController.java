package com.beyond.bycontract.subscription.presentation.controller;

import com.beyond.bycontract.subscription.application.service.StripeWebhookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

    private final StripeWebhookService stripeWebhookService;

    @Value("${stripe.webhook.secret}")
    private String endPointSecret;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, // Doit rester un String brut
                                                      @RequestHeader("Stripe-Signature") String sigHeader ) {
        Event event;

        try {
            // 1. Vérification de la signature cryptographique (Le cœur de la sécurité)
            event = Webhook.constructEvent(payload, sigHeader, endPointSecret);
        }catch (SignatureVerificationException e) {
            log.error("Tentative de Webhook invalide (Signature incorrecte)", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signature invalide");
        } catch (Exception e) {
            log.error("Erreur lors de la lecture du payload", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payload invalide");
        }

        // 2. Transmettre l'événement sécurisé à ton service métier
        try {
            stripeWebhookService.processWebhookEvent(event);
        }catch (Exception e) {
            log.error("Erreur metier lors du traitement de l evenement {}", event.getType(), e);
            // On renvoie 500 pour que Stripe retente l'envoi de l'événement plus tard
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // 3. Répondre 200 OK à Stripe pour dire "Message bien reçu, ne le renvoie plus"
        return ResponseEntity.ok().build();
    }
}
