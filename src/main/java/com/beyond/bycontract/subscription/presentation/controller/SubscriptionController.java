package com.beyond.bycontract.subscription.presentation.controller;

import com.beyond.bycontract.shared.utils.CustomUserDetails;
import com.beyond.bycontract.subscription.application.dto.FacturationInformationsResponse;
import com.beyond.bycontract.subscription.application.dto.InvoiceResponse;
import com.beyond.bycontract.subscription.application.dto.SubscriptionStatusResponse;
import com.beyond.bycontract.subscription.application.service.SubscriptionService;
import com.beyond.bycontract.subscription.presentation.dto.CheckoutRequest;
import com.beyond.bycontract.subscription.presentation.dto.CustomerPortalRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/checkout")
    public ResponseEntity<?> createCheckout(@RequestBody CheckoutRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
        System.out.println("🟢 REQUÊTE FRONTEND REÇUE ! User: " + (currentUser != null ? currentUser.getUsername() : "ANONYME"));
        // En production, le priceId (ex: price_1M...) peut être stocké en base ou dans les properties
        String checkoutUrl = subscriptionService.createCheckoutSession(
                currentUser.getId(),
                request.idPrice(),
                request.successUrl(),
                request.cancelUrl()
        );

        return ResponseEntity.ok(Map.of("url", checkoutUrl));
    }

    @PostMapping("/customer-portal")
    public ResponseEntity<?> openCustomerPortal(@RequestBody CustomerPortalRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
        String portalUrl = subscriptionService.createCustomerPortalSession(currentUser.getId(), request.returnUrl());
        return ResponseEntity.ok(Map.of("url", portalUrl));
    }

    @GetMapping("/status")
    public ResponseEntity<SubscriptionStatusResponse> getMySubscriptionStatus(@AuthenticationPrincipal CustomUserDetails currentUser) {
        SubscriptionStatusResponse statusResponse = subscriptionService.getSubscriptionData(currentUser.getId());
        return ResponseEntity.ok(statusResponse);
    }

    @GetMapping("/me/invoices")
    public ResponseEntity<List<InvoiceResponse>> getMyInvoices(@AuthenticationPrincipal CustomUserDetails currentUser) {
        List<InvoiceResponse> invoices = subscriptionService.getUserInvoices(currentUser.getId());
        return ResponseEntity.ok(invoices);
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelSubscription(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        subscriptionService.cancelSubscription(currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reactivate")
    public ResponseEntity<Void> reactivateSubscription(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        subscriptionService.reactivateSubscription(currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/details")
    public ResponseEntity<FacturationInformationsResponse> getFacturationInformations(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        return ResponseEntity.ok(subscriptionService.getFacturationInformations(currentUser.getId()));
    }

    @PutMapping("/details")
    public ResponseEntity<FacturationInformationsResponse> updateFacturationInformations(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody FacturationInformationsResponse dto
    ) {
        return ResponseEntity.ok(subscriptionService.updateFacturationInformations(currentUser.getId(), dto));
    }

}
