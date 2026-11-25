package com.beyond.bycontract.auth.infrastructure.repository;

import com.beyond.bycontract.auth.infrastructure.entity.PendingRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface JpaPendingRegistrationRepository extends JpaRepository<PendingRegistrationEntity, UUID> {
    Optional<PendingRegistrationEntity> findByEmail(String email);
    void deleteByEmail(String email);
    void deleteAllByExpiresAtBefore(LocalDateTime now);
}
