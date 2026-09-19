package com.beyond.bycontract.company.infrastructure.repository;

import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaCompanyRepository extends JpaRepository<CompanyEntity, UUID> {
	boolean existsBySiret(String siret);

	Optional<CompanyEntity> findBySiret(String siret);

	// Spring Data génère : SELECT * FROM companies WHERE id_creator = ?
	@Query("SELECT c FROM CompanyEntity c WHERE c.creator.id = :idCreator")
	List<CompanyEntity> findAllByIdCreator(UUID idCreator);
}
