package com.beyond.bycontract.auth.adapter.dto;

public record OtpRequestedEvent(
        String email,
        String firstName,
        String otpCode
) {
}
