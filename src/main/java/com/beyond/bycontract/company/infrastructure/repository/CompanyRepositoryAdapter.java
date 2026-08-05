package com.beyond.bycontract.company.infrastructure.repository;

import com.beyond.bycontract.company.domain.model.Company;
import com.beyond.bycontract.company.domain.repository.CompanyRepository;
import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
import com.beyond.bycontract.company.infrastructure.mapper.CompanyPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryAdapter implements CompanyRepository {

	private final JpaCompanyRepository jpaRepository;

	@Override
	public Company create(Company company) {
		CompanyEntity savedEntity = jpaRepository.save(CompanyPersistenceMapper.toEntity(company));
		return CompanyPersistenceMapper.reconstituteDomain(savedEntity);
	}

	@Override
	public List<Company> getAllCompanies() {
		return jpaRepository.findAll().stream().map(CompanyPersistenceMapper::reconstituteDomain).toList();
	}

	@Override
	public Optional<Company> findBySiret(String siret) {
		return jpaRepository.findBySiret(siret).map(CompanyPersistenceMapper::reconstituteDomain);
	}

	@Override
	public boolean existsBySiret(String siret) {
		return jpaRepository.existsBySiret(siret);
	}

	@Override
	public void deleteById(UUID id) {
		jpaRepository.deleteById(id);
	}
}
