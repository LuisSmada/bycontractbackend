package com.beyond.bycontract.subscription.presentation.dto;

public record CheckoutRequest(
        String idPrice,
        String successUrl,
        String cancelUrl
) {
}
