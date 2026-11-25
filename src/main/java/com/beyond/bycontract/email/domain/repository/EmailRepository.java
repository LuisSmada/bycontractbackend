package com.beyond.bycontract.email.domain.repository;

public interface EmailRepository {
    void sendPasswordResetEmail(String email, String resetUrl);
    void sendTrialEndingEmail(String toEmail, String firstName, String frontEndUrl);
    void sendPaymentFailedEmail(String toEmail, String firstName, String invoiceUrl);
    void sendSubscriptionCanceledEmail(String toEmail, String firstName, String frontEndUrl);
    void sendTrialExpiredEmail(String toEmail, String firstName, String frontEndUrl);

    void sendOtpEmail(String to, String firstName, String otpCode);
}
