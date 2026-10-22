package com.beyond.bycontract.company.infrastructure.repository;

import com.beyond.bycontract.company.infrastructure.entity.ParticularStakeholderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaParticularStakeholderRepository extends JpaRepository<ParticularStakeholderEntity, UUID> {
    boolean existsByEmail(String email);
}
