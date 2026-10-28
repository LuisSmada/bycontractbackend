package com.beyond.bycontract.auth.infrastructure.repository;

import com.beyond.bycontract.auth.infrastructure.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface JpaPasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, UUID> {
    Optional<PasswordResetTokenEntity> findByToken(String token);
    void deleteByToken(String token);
    @Modifying
    @Query("DELETE FROM PasswordResetTokenEntity t WHERE t.user.id = :idUser")
    void deleteByIdUser(UUID idUser);
}
