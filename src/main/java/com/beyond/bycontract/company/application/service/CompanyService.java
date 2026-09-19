package com.beyond.bycontract.company.application.service;

import com.beyond.bycontract.company.application.dto.CompanyResponse;
import com.beyond.bycontract.company.application.dto.CreateCompanyCommand;
import com.beyond.bycontract.company.domain.exception.CompanyNotFoundException;
import com.beyond.bycontract.company.domain.exception.SiretAlreadyExistsException;
import com.beyond.bycontract.company.domain.model.Company;
import com.beyond.bycontract.company.domain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

	private final CompanyRepository repository;

	@Transactional
	public CompanyResponse create(CreateCompanyCommand command) {

		if (repository.existsBySiret(command.siret())) {
			throw new SiretAlreadyExistsException(command.siret());
		}

		Company company = Company.create(
				command.name(),
				command.idCreator(),
				command.siret(),
				command.address()
		);

		Company savedCompany = repository.create(company);

		return new CompanyResponse(
				savedCompany.getId(),
				savedCompany.getName(),
				savedCompany.getSiret(),
				savedCompany.getAddress()
		);

	}

	@Transactional(readOnly = true)
	public List<CompanyResponse> getAllCompanies() {
		List<Company> allCompanies = repository.getAllCompanies();
		return allCompanies.stream().map(company -> new CompanyResponse(
				company.getId(),
				company.getName(),
				company.getSiret(),
				company.getAddress()
		)).toList();
	}

	public void deleteById(UUID id) {
		repository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public CompanyResponse getCompanyById(UUID id) {
		Company company = repository.getCompanyById(id).orElseThrow(() -> new CompanyNotFoundException("No company found with id: " + id));
		return new CompanyResponse(
				company.getId(),
				company.getName(),
				company.getSiret(),
				company.getAddress()
		);
	}

	public List<CompanyResponse> getAllCompaniesByIdCreator(UUID idCreator) {
		List<Company> allCompanies = repository.getCompaniesByIdCreator(idCreator);
		return allCompanies.stream().map(company -> new CompanyResponse(
				company.getId(),
				company.getName(),
				company.getSiret(),
				company.getAddress()
		)).toList();
	}

}
