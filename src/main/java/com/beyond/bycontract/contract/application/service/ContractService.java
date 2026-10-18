package com.beyond.bycontract.contract.application.service;

import com.beyond.bycontract.company.domain.exception.CompanyNotFoundException;
import com.beyond.bycontract.company.domain.model.Company;
import com.beyond.bycontract.company.domain.repository.CompanyRepository;
import com.beyond.bycontract.contract.application.dto.ContractResponse;
import com.beyond.bycontract.contract.application.dto.CreateContractCommand;
import com.beyond.bycontract.contract.application.dto.FindContractResponse;
import com.beyond.bycontract.contract.application.dto.UpdateContractCommand;
import com.beyond.bycontract.contract.domain.exception.ContractNotfoundException;
import com.beyond.bycontract.contract.domain.model.Contract;
import com.beyond.bycontract.contract.domain.model.ContractStatus;
import com.beyond.bycontract.contract.domain.repository.ContractRepository;
import com.beyond.bycontract.contract.presentation.dto.UpdateContractRequest;
import com.beyond.bycontract.shared.infrastructure.S3StorageService;
import com.beyond.bycontract.user.domain.exception.UserNotFoundException;
import com.beyond.bycontract.user.domain.model.User;
import com.beyond.bycontract.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {

	private final ContractRepository repository;
	private final UserRepository userRepository;
	private final CompanyRepository companyRepository;
	private final S3StorageService s3StorageService;

	@Transactional
	public ContractResponse create(CreateContractCommand command) throws Exception {

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
				savedContract.getContractType(),
				new ContractResponse.AuthorDto(author.getId(), author.getFirstName(), author.getLastName()),
				new ContractResponse.CompanyDto(stakeholder.getId(), stakeholder.getName()),
				savedContract.getCreatedAt(),
				savedContract.getModifiedAt(),
				savedContract.getExpirationDate(),
				savedContract.getIdTemplate()
		);

	}

	public void deleteContractById(UUID id) {
		repository.deleteContractById(id);
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
					contract.getContractType(),
					new ContractResponse.AuthorDto(author.getId(), author.getFirstName(), author.getLastName()),
					new ContractResponse.CompanyDto(stakeholder.getId(), stakeholder.getName()),
					contract.getCreatedAt(),
					contract.getModifiedAt(),
					contract.getExpirationDate(),
					contract.getIdTemplate()
			);
		}).toList();
	}

	private FindContractResponse buildFindContractResponse(Contract contract) {
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
				contract.getVersion(),
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
						contract.getContractContent().getSignedPdfKey(),
						contract.getContractContent().getModifiedAt()
				),
				contract.getIdTemplate()
		);
	}


	public FindContractResponse getContractById(UUID id) {
		Contract contract = repository.getContractById(id).orElseThrow(() -> new ContractNotfoundException(id));
		return buildFindContractResponse(contract);

	}

	public FindContractResponse updateContractById(UpdateContractCommand command) {
		Contract contract = repository.getContractById(command.idContract()).orElseThrow(() -> new ContractNotfoundException(command.idContract()));

		if (!contract.getIdAuthor().equals(command.idRequester())) {
			throw new AccessDeniedException("You are not allowed to update this contract");
		}

		contract.update(
				command.name(),
				command.contractType(),
				command.contractStatus(),
				command.idCompany(),
				command.effectiveDate(),
				command.expirationDate(),
				command.autoRenew(),
				command.value(),
				command.bodyJson(),
				command.bodyText()
		);

		Contract updatedContract = repository.updateContract(contract);

		return buildFindContractResponse(updatedContract);
	}

	public FindContractResponse uploadSignedContract(UUID idContract, MultipartFile file, UUID idRequester) {
		// 1. Récupérer le contrat
		Contract contract = repository.getContractById(idContract)
				.orElseThrow(() -> new ContractNotfoundException(idContract));

		// 2. Sécurité : Vérifier que l'utilisateur a le droit (IDOR)
		if (!contract.getIdAuthor().equals(idRequester)) {
			throw new AccessDeniedException("Vous n'avez pas l'autorisation de modifier ce contrat");
		}

		// 3. Vérifier que c'est bien un PDF
		if (!"application/pdf".equals(file.getContentType())) {
			throw new IllegalArgumentException("Seuls les fichiers PDF sont acceptés");
		}

		String s3Key = s3StorageService.uploadSignedContract(idContract, file);
		contract.markAsSigned(s3Key);

		Contract updatedContract = repository.updateContract(contract);

		return buildFindContractResponse(updatedContract);
	}

	public String getSignedPdfUrl(UUID idContract, UUID idRequester) {
		Contract contract = repository.getContractById(idContract)
				.orElseThrow(() -> new ContractNotfoundException(idContract));

		// 2. Sécurité : Vérifier que l'utilisateur a le droit (IDOR)
		if (!contract.getIdAuthor().equals(idRequester)) {
			throw new AccessDeniedException("Vous n'avez pas l'autorisation de modifier ce contrat");
		}

		String s3Key = contract.getContractContent().getSignedPdfKey();
		if(s3Key == null) {
			throw new IllegalStateException("Aucun PDF signé n'existe pour ce contrat.");
		}

		return s3StorageService.getPresignedUrl(s3Key);
	}
}
