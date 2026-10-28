package com.beyond.bycontract.email.domain.repository;

public interface EmailRepository {
    void sendPasswordResetEmail(String email, String resetUrl);
}
