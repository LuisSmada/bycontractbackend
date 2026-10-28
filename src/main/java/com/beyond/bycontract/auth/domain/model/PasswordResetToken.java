package com.beyond.bycontract.auth.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordResetToken {
    private UUID id;
    private String token;
    private UUID idUser;
    private LocalDateTime expiryDate;

    public PasswordResetToken() {
    }

    public PasswordResetToken(UUID id, String token, UUID idUser, LocalDateTime expiryDate) {
        this.id = id;
        this.token = token;
        this.idUser = idUser;
        this.expiryDate = expiryDate;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public UUID getIdUser() {
        return idUser;
    }

    public String getToken() {
        return token;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }
}
