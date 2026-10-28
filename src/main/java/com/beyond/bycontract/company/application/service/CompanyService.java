package com.beyond.bycontract.company.application.service;

import com.beyond.bycontract.company.application.dto.CompanyResponse;
import com.beyond.bycontract.company.application.dto.CreateCompanyCommand;
import com.beyond.bycontract.company.application.dto.UpdateCompanyCommand;
import com.beyond.bycontract.company.domain.exception.CompanyNotFoundException;
import com.beyond.bycontract.company.domain.exception.SiretAlreadyExistsException;
import com.beyond.bycontract.company.domain.model.Company;
import com.beyond.bycontract.company.domain.model.MainContactCompany;
import com.beyond.bycontract.company.domain.repository.CompanyRepository;
import com.beyond.bycontract.shared.utils.AuthorizationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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

		MainContactCompany contact = new MainContactCompany(
				command.mainContact().firstName(),
				command.mainContact().lastName(),
				command.mainContact().email(),
				command.mainContact().phone()
		);

		Company company = Company.create(
				command.name(),
				command.idCreator(),
				command.siret(),
				command.address(),
				contact
		);

		Company savedCompany = repository.create(company);

		return new CompanyResponse(
				savedCompany.getId(),
				savedCompany.getName(),
				savedCompany.getSiret(),
				savedCompany.getAddress(),
				new CompanyResponse.MainContactResponse(
						savedCompany.getMainContactCompany().getFirstName(),
						savedCompany.getMainContactCompany().getLastName(),
						savedCompany.getMainContactCompany().getEmail(),
						savedCompany.getMainContactCompany().getPhone()
				)
		);

	}

	@Transactional(readOnly = true)
	public List<CompanyResponse> getAllCompanies(UUID idCreator) {
		List<Company> allCompanies = idCreator == null ? repository.getAllCompanies() : repository.getCompaniesByIdCreator(idCreator);
		return allCompanies.stream().map(company -> new CompanyResponse(
				company.getId(),
				company.getName(),
				company.getSiret(),
				company.getAddress(),
				new CompanyResponse.MainContactResponse(
						company.getMainContactCompany().getFirstName(),
						company.getMainContactCompany().getLastName(),
						company.getMainContactCompany().getEmail(),
						company.getMainContactCompany().getPhone()
				)
		)).toList();
	}

	public void deleteById(UUID id, UUID idRequester) {
		Company company = repository.getCompanyById(id).orElseThrow(() -> new CompanyNotFoundException(id));
		AuthorizationUtils.verifyOwnership(company.getIdCreator(), idRequester);
		repository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public CompanyResponse getCompanyById(UUID id, UUID idRequester) {
		Company company = repository.getCompanyById(id).orElseThrow(() -> new CompanyNotFoundException(id));
		AuthorizationUtils.verifyOwnership(company.getIdCreator(), idRequester);
		return new CompanyResponse(
				company.getId(),
				company.getName(),
				company.getSiret(),
				company.getAddress(),
				new CompanyResponse.MainContactResponse(
						company.getMainContactCompany().getFirstName(),
						company.getMainContactCompany().getLastName(),
						company.getMainContactCompany().getEmail(),
						company.getMainContactCompany().getPhone()
				)
		);
	}

	@Transactional
	public CompanyResponse updateCompanyById( UpdateCompanyCommand command) {
		Company company = repository.getCompanyById(command.idCompany()).orElseThrow(() -> new CompanyNotFoundException(command.idCompany()));
		AuthorizationUtils.verifyOwnership(company.getIdCreator(), command.idRequester());

		//UPDATE VALUES OF THE COMPANY ITSELF
		company.update(
				command.name(),
				command.siret(),
				command.address()
		);

		//UPDATE VALUES OF THE MAIN CONTACT
		if (command.mainContact() != null) {
			company.getMainContactCompany().update(
					command.mainContact().firstName(),
					command.mainContact().lastName(),
					command.mainContact().email(),
					command.mainContact().phone()
			);
		}
		Company updatedCompany = repository.updateCompanyById(company);

		return new CompanyResponse(
				updatedCompany.getId(),
				updatedCompany.getName(),
				updatedCompany.getSiret(),
				updatedCompany.getAddress(),
				new CompanyResponse.MainContactResponse(
						updatedCompany.getMainContactCompany().getFirstName(),
						updatedCompany.getMainContactCompany().getLastName(),
						updatedCompany.getMainContactCompany().getEmail(),
						updatedCompany.getMainContactCompany().getPhone()
				)
		);
	}

}
