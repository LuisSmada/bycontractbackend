package com.beyond.bycontract.contract.application.service;

import com.beyond.bycontract.company.domain.exception.CompanyNotFoundException;
import com.beyond.bycontract.company.domain.model.Company;
import com.beyond.bycontract.company.domain.repository.CompanyRepository;
import com.beyond.bycontract.contract.application.dto.ContractResponse;
import com.beyond.bycontract.contract.application.dto.CreateContractCommand;
import com.beyond.bycontract.contract.application.dto.FindContractResponse;
import com.beyond.bycontract.contract.domain.exception.ContractNotfoundException;
import com.beyond.bycontract.contract.domain.model.Contract;
import com.beyond.bycontract.contract.domain.repository.ContractRepository;
import com.beyond.bycontract.user.domain.exception.UserNotFoundException;
import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {

	private final ContractRepository repository;
	private final UserRepository userRepository;
	private final CompanyRepository companyRepository;

	@Transactional
	public ContractResponse create(CreateContractCommand command)  {

		User author = userRepository.getUserById(command.idAuthor()).orElseThrow(() -> new UserNotFoundException("No user with id: " + command.idAuthor()));
		Company stakeholder = companyRepository.getCompanyById(command.idCompany()).orElseThrow(() -> new CompanyNotFoundException("No company found with id: " + command.idCompany()));

		//VERIFY THAT THE STAKEHOLDER IS REALLY FROM THE CONTACT OF THE CURRENT USER
		if(!stakeholder.getIdCreator().equals(author.getId())) {
			throw new AccessDeniedException("You do not have the authorization to use this stakeholder");
		}

		Contract contract = Contract.create(
				command.name(),
				command.contractType(),
				command.contractStatus(),
				command.idCompany(),
				command.idAuthor(),
				command.idTemplate(),
				command.effectiveDate(),
				command.expirationDate(),
				command.autoRenew(),
				command.value(),
				command.bodyJson(),
				command.bodyText()
		);

		Contract savedContract = repository.create(contract);

		return new ContractResponse(
				savedContract.getId(),
				savedContract.getName(),
				savedContract.getContractStatus(),
				new ContractResponse.AuthorDto(author.getId(), author.getFirstName(), author.getLastName()),
				new ContractResponse.CompanyDto(stakeholder.getId(), stakeholder.getName()),
				savedContract.getCreatedAt(),
				savedContract.getModifiedAt()
		);

	}

	public List<ContractResponse> getAllContracts() {

		List<Contract> contracts = repository.getAllContracts();

		Set<UUID> idAuthors = contracts.stream().map(Contract::getIdAuthor).collect(Collectors.toSet());
		List<User> authors = userRepository.getUsersByIds(idAuthors);
		Map<UUID, User> authorsById = authors.stream().collect(Collectors.toMap(
				User::getId,
				Function.identity()
		));

		Set<UUID> idStakeholders = contracts.stream().map(Contract::getIdCompany).collect(Collectors.toSet());
		List<Company> companies = companyRepository.getCompaniesByIds(idStakeholders);
		Map<UUID, Company> companiesById = companies.stream().collect(Collectors.toMap(
				Company::getId,
				Function.identity()
		));

		return contracts.stream().map(contract -> {
			User author = Optional.ofNullable(authorsById.get(contract.getIdAuthor())).orElseThrow(() -> new UserNotFoundException("No user found with id: " + contract.getIdAuthor()));
			Company stakeholder = Optional.ofNullable(companiesById.get(contract.getIdCompany())).orElseThrow(() -> new UserNotFoundException("No company found with id: " + contract.getIdAuthor()));
			return new ContractResponse(
					contract.getId(),
					contract.getName(),
					contract.getContractStatus(),
					new ContractResponse.AuthorDto(author.getId(), author.getFirstName(), author.getLastName()),
					new ContractResponse.CompanyDto(stakeholder.getId(), stakeholder.getName()),
					contract.getCreatedAt(),
					contract.getModifiedAt()
			);
		}).toList();
	}


	public FindContractResponse getContractById(UUID id) {
		Contract contract = repository.getContractById(id).orElseThrow(() -> new ContractNotfoundException(id));
		User author = userRepository.getUserById(contract.getIdAuthor()).orElseThrow(() -> new UserNotFoundException(contract.getIdAuthor()));
		Company stakeholder = companyRepository.getCompanyById(contract.getIdCompany()).orElseThrow(() -> new CompanyNotFoundException(contract.getIdCompany()));

		return new FindContractResponse(
				contract.getId(),
				contract.getAutoRenew(),
				contract.getContractStatus(),
				contract.getContractType(),
				contract.getCreatedAt(),
				contract.getModifiedAt(),
				contract.getEffectiveDate(),
				contract.getExpirationDate(),
				contract.getName(),
				contract.getValue(),
				new FindContractResponse.AuthorDto(author.getId(), author.getFirstName(), author.getLastName()),
				new FindContractResponse.CompanyDto(stakeholder.getId(), stakeholder.getName(), stakeholder.getSiret(), new FindContractResponse.MainContactDtoFC(
						stakeholder.getMainContactCompany().getFirstName(),
						stakeholder.getMainContactCompany().getLastName(),
						stakeholder.getMainContactCompany().getEmail(),
						stakeholder.getMainContactCompany().getPhone()
				) ),
				new FindContractResponse.ContractContentDto(
						contract.getContractContent().getBody(),
						contract.getContractContent().getPlainText(),
						contract.getContractContent().getSignedPdfUrl(),
						contract.getContractContent().getModifiedAt()
				),
				contract.getIdTemplate()
		);
	}
}
