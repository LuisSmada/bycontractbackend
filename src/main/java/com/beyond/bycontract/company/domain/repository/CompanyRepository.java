package com.beyond.bycontract.company.domain.repository;

import com.beyond.bycontract.company.domain.model.Company;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {

	Company create(Company company);

	List<Company> getAllCompanies();

	Optional<Company> findBySiret(String siret);

	boolean existsBySiret(String siret);

	void deleteById(UUID id);
}
