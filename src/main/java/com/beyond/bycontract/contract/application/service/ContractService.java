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
	public ContractResponse create(CreateContractCommand command) {

		Contract contract = Contract.create(
				command.name(),
				command.contractType(),
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

		User author = userRepository.getUserById(savedContract.getIdAuthor()).orElseThrow(() -> new UserNotFoundException("No user with id: " + savedContract.getIdAuthor()));
		Company stakeholder = companyRepository.getCompanyById(savedContract.getIdCompany()).orElseThrow(() -> new CompanyNotFoundException("No company found with id: " + savedContract.getIdCompany()));

		return new ContractResponse(
				savedContract.getId(),
				savedContract.getName(),
				savedContract.getContractStatus(),
				new ContractResponse.AuthorDto(author.getFirstName(), author.getLastName()),
				new ContractResponse.CompanyDto(stakeholder.getName()),
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
					new ContractResponse.AuthorDto(author.getFirstName(), author.getLastName()),
					new ContractResponse.CompanyDto(stakeholder.getName()),
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
				new FindContractResponse.CompanyDto(stakeholder.getId(), stakeholder.getName()),
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
