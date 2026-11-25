package com.beyond.bycontract.auth.domain.repository;

import com.beyond.bycontract.auth.domain.model.PendingRegistration;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PendingRegistrationRepository {
    PendingRegistration save(PendingRegistration pendingRegistration);
    Optional<PendingRegistration> getByEmail(String email);
    void deleteByEmail(String email);
    void deleteAllByExpiresAtBefore(LocalDateTime now);
}
