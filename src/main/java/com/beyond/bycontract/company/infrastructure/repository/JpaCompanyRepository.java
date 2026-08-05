package com.beyond.bycontract.company.infrastructure.repository;

import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaCompanyRepository extends JpaRepository<CompanyEntity, UUID> {
	boolean existsBySiret(String siret);

	Optional<CompanyEntity> findBySiret(String siret);
}
