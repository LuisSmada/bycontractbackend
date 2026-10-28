package com.beyond.bycontract.auth.domain.repository;

import com.beyond.bycontract.auth.domain.model.PasswordResetToken;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetTokenRepository {
    void save(PasswordResetToken passwordResetToken);
    Optional<PasswordResetToken> findByToken(String token);
    void  deleteByToken(String token);
    void deleteByIdUser(UUID userId);
}
