package com.beyond.bycontract.contract.application.service;

import com.beyond.bycontract.company.domain.exception.CompanyNotFoundException;
import com.beyond.bycontract.company.domain.model.Company;
import com.beyond.bycontract.company.domain.model.ParticularStakeholder;
import com.beyond.bycontract.company.domain.repository.CompanyRepository;
import com.beyond.bycontract.company.domain.repository.ParticularStakeholderRepository;
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
import com.beyond.bycontract.shared.utils.AuthorizationUtils;
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
	private final ParticularStakeholderRepository particularStakeholderRepository;
	private final S3StorageService s3StorageService;

	@Transactional
	public ContractResponse create(CreateContractCommand command) throws Exception {

		User author = userRepository.getUserById(command.idAuthor()).orElseThrow(() -> new UserNotFoundException("No user with id: " + command.idAuthor()));
		Company stakeholder = companyRepository.getCompanyById(command.idCompany()).orElseThrow(() -> new CompanyNotFoundException("No company found with id: " + command.idCompany()));
		ParticularStakeholder particularStakeholder = particularStakeholderRepository.getParticularStakeholderById(command.idParticularStakeholder()).orElseThrow(() -> new RuntimeException("No particular stakeholder found with id: " + command.idParticularStakeholder()));

		//VERIFY THAT THE STAKEHOLDER IS REALLY FROM THE CONTACT OF THE CURRENT USER
		if(!stakeholder.getIdCreator().equals(author.getId())) {
			throw new AccessDeniedException("You do not have the authorization to use this stakeholder");
		}

		Contract contract = Contract.create(
				command.name(),
				command.contractType(),
				command.contractStatus(),
				command.idCompany(),
				command.idParticularStakeholder(),
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
				new ContractResponse.ParticularStakeholderDto(particularStakeholder.getId(), particularStakeholder.getFirstName(), particularStakeholder.getLastName()),
				savedContract.getCreatedAt(),
				savedContract.getModifiedAt(),
				savedContract.getExpirationDate(),
				savedContract.getIdTemplate(),
				savedContract.getValue()
		);

	}

	public void deleteContractById(UUID id) {
		repository.deleteContractById(id);
	}

	public List<ContractResponse> getAllContracts(UUID idCreator) {

		List<Contract> contracts = (idCreator == null)
				? repository.getAllContracts()
				: repository.getAllContractsByIdCreator(idCreator);

		//AUTHORS
		Set<UUID> idAuthors = contracts.stream()
				.map(Contract::getIdAuthor)
				.collect(Collectors.toSet());

		Map<UUID, User> authorsById = userRepository.getUsersByIds(idAuthors).stream().collect(Collectors.toMap(
				User::getId,
				Function.identity()
		));

		//COMPANIES
		Set<UUID> idCompanies = contracts.stream()
				.map(Contract::getIdCompany)
				.collect(Collectors.toSet());

		Map<UUID, Company> companiesById = idCompanies.isEmpty()
				? Map.of()
				: companyRepository.getCompaniesByIds(idCompanies).stream()
						.collect(Collectors.toMap(Company::getId, Function.identity()
		));

		//PARTICULAR STAKEHOLDERS
		Set<UUID> idParticularStakeholders = contracts.stream()
				.map(Contract::getIdParticularStakeholder)
				.collect(Collectors.toSet());

		Map<UUID, ParticularStakeholder> particularStakeholdersById = idParticularStakeholders.isEmpty()
				? Map.of()
				: particularStakeholderRepository.getParticularStakeholdersByIds(idParticularStakeholders).stream()
						.collect(Collectors.toMap(ParticularStakeholder::getId, Function.identity()
		));

		return contracts.stream().map(contract -> {
			User author = Optional.ofNullable(authorsById.get(contract.getIdAuthor())).orElseThrow(() -> new UserNotFoundException("No user found with id: " + contract.getIdAuthor()));

			ContractResponse.CompanyDto companyDto = null;
			if(contract.getIdCompany() != null) {
				Company company = Optional.ofNullable(companiesById.get(contract.getIdCompany())).orElseThrow(() -> new CompanyNotFoundException("No company found with id: " + contract.getIdCompany()));
				companyDto = new ContractResponse.CompanyDto(company.getId(), company.getName());
			}

			ContractResponse.ParticularStakeholderDto particularStakeholderDto = null;
			if(contract.getIdParticularStakeholder() != null) {
				ParticularStakeholder particularStakeholder = Optional.ofNullable(particularStakeholdersById.get(contract.getIdParticularStakeholder())).orElseThrow(() -> new RuntimeException("No particular stakeholder found with id: " + contract.getIdParticularStakeholder()));
				particularStakeholderDto = new ContractResponse.ParticularStakeholderDto(particularStakeholder.getId(), particularStakeholder.getFirstName(), particularStakeholder.getLastName());
			}

			return new ContractResponse(
					contract.getId(),
					contract.getName(),
					contract.getContractStatus(),
					contract.getContractType(),
					new ContractResponse.AuthorDto(author.getId(), author.getFirstName(), author.getLastName()),
					companyDto,
					particularStakeholderDto,
					contract.getCreatedAt(),
					contract.getModifiedAt(),
					contract.getExpirationDate(),
					contract.getIdTemplate(),
					contract.getValue()
			);
		}).toList();
	}



	private FindContractResponse buildFindContractResponse(Contract contract) {
		User author = userRepository.getUserById(contract.getIdAuthor()).orElseThrow(() -> new UserNotFoundException(contract.getIdAuthor()));
		Company stakeholder = companyRepository.getCompanyById(contract.getIdCompany()).orElseThrow(() -> new CompanyNotFoundException(contract.getIdCompany()));
		ParticularStakeholder particularStakeholder = particularStakeholderRepository.getParticularStakeholderById(contract.getIdParticularStakeholder()).orElseThrow(() -> new RuntimeException("No particular stakeholder found with id: " + contract.getIdParticularStakeholder()));

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
				new FindContractResponse.ParticularStakeholderDto(
						particularStakeholder.getId(),
						particularStakeholder.getFirstName(),
						particularStakeholder.getLastName()
				),
				new FindContractResponse.ContractContentDto(
						contract.getContractContent().getBody(),
						contract.getContractContent().getPlainText(),
						contract.getContractContent().getSignedPdfKey(),
						contract.getContractContent().getModifiedAt()
				),
				contract.getIdTemplate()
		);
	}


	public FindContractResponse getContractById(UUID id, UUID idRequester) {
		Contract contract = repository.getContractById(id).orElseThrow(() -> new ContractNotfoundException(id));
		AuthorizationUtils.verifyOwnership(contract.getIdAuthor(), idRequester);
		return buildFindContractResponse(contract);

	}

	public FindContractResponse updateContractById(UpdateContractCommand command) {
		Contract contract = repository.getContractById(command.idContract()).orElseThrow(() -> new ContractNotfoundException(command.idContract()));
		AuthorizationUtils.verifyOwnership(contract.getIdAuthor(), command.idRequester());

		if (command.idCompany() != null) {
			Company company = companyRepository.getCompanyById(command.idCompany())
					.orElseThrow(() -> new CompanyNotFoundException("No company found with id: " + command.idCompany()));
			AuthorizationUtils.verifyOwnership(company.getIdCreator(), command.idRequester());
		} else if (command.idParticularStakeholder() != null) {
			ParticularStakeholder particular = particularStakeholderRepository.getParticularStakeholderById(command.idParticularStakeholder())
					.orElseThrow(() -> new RuntimeException("No particular stakeholder found with id: " + command.idParticularStakeholder()));
			AuthorizationUtils.verifyOwnership(particular.getIdCreator(), command.idRequester());
		}

		contract.update(
				command.name(),
				command.contractType(),
				command.contractStatus(),
				command.idCompany(),
				command.idParticularStakeholder(),
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
		Contract contract = repository.getContractById(idContract).orElseThrow(() -> new ContractNotfoundException(idContract));
		AuthorizationUtils.verifyOwnership(contract.getIdAuthor(), idRequester);

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
		Contract contract = repository.getContractById(idContract).orElseThrow(() -> new ContractNotfoundException(idContract));

		AuthorizationUtils.verifyOwnership(contract.getIdAuthor(), idRequester);

		String s3Key = contract.getContractContent().getSignedPdfKey();
		if(s3Key == null) {
			throw new IllegalStateException("Aucun PDF signé n'existe pour ce contrat.");
		}

		return s3StorageService.getPresignedUrl(s3Key);
	}
}
