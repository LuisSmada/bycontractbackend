package com.beyond.bycontract.subscription.application.dto;

import java.time.LocalDateTime;

public record SubscriptionStatusResponse(
        String status,
        LocalDateTime trialEndDate,
        String cardBrand,
        String cardLast4,
        String planName,
        Double planPrice,
        LocalDateTime currentPeriodEnd
) {
}
