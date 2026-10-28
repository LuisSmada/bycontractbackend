package com.beyond.bycontract.company.infrastructure.repository;

import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
import com.beyond.bycontract.company.infrastructure.entity.ParticularStakeholderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JpaParticularStakeholderRepository extends JpaRepository<ParticularStakeholderEntity, UUID> {
    boolean existsByEmail(String email);

    @Query("SELECT p FROM ParticularStakeholderEntity p WHERE p.creator.id = :idCreator")
    List<ParticularStakeholderEntity> findAllByIdCreator(UUID idCreator);
}
