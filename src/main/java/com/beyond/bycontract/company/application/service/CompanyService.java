package com.beyond.bycontract.company.application.service;

import com.beyond.bycontract.company.application.dto.CompanyResponse;
import com.beyond.bycontract.company.application.dto.CreateCompanyCommand;
import com.beyond.bycontract.company.domain.exception.SiretAlreadyExistsException;
import com.beyond.bycontract.company.domain.model.Company;
import com.beyond.bycontract.company.domain.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
