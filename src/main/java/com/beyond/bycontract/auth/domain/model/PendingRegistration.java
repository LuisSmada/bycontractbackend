package com.beyond.bycontract.auth.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PendingRegistration {

    private static final int OTP_EXPIRATION_MINUTES = 10;
    private static final int MAX_ATTEMPTS = 5;
    private static final int RESEND_COOLDOWN_SECONDS = 60;


    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String passwordHash;
    private String otpHash;
    private LocalDateTime expiresAt;
    private LocalDateTime lastSentAt;
    private int attempts;
    private LocalDateTime createdAt;

    public static PendingRegistration create(
            String email, String firstName, String lastName,
            String passwordHash, String otpHash, LocalDateTime now
    ) {
        return new PendingRegistration(
                null,
                email,
                firstName,
                lastName,
                passwordHash,
                otpHash,
                now.plusMinutes(OTP_EXPIRATION_MINUTES),
                now,
                0,
                null
        );
    }

    // Méthode métier : l'utilisateur relance une inscription avec le même email
    public void restart(String firstName, String lastName, String passwordHash, String otpHash, LocalDateTime now) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.otpHash = otpHash;
        this.expiresAt = now.plusMinutes(OTP_EXPIRATION_MINUTES);
        this.lastSentAt = now;
        this.attempts = 0;
    }

    public void regenerateOtp(String newOtpHash, LocalDateTime now) {
        if (this.lastSentAt.plusSeconds(RESEND_COOLDOWN_SECONDS).isAfter(now)) {
            throw new IllegalStateException("Veuillez patienter 60 secondes avant de demander un nouveau code.");
        }
        this.otpHash = newOtpHash;
        this.expiresAt = now.plusMinutes(OTP_EXPIRATION_MINUTES);
        this.lastSentAt = now;
        this.attempts = 0;
    }

    // Méthode métier : échec de vérification
    public void incrementAttempts() {
        this.attempts++;
    }

    public boolean isExpired(LocalDateTime now) {
        return this.expiresAt.isBefore(now);
    }

    public boolean hasReachedMaxAttempts() {
        return this.attempts >= MAX_ATTEMPTS;
    }

}
