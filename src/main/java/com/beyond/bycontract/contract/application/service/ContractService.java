package com.beyond.bycontract.contract.application.service;

import com.beyond.bycontract.contract.application.dto.ContractResponse;
import com.beyond.bycontract.contract.application.dto.CreateContractCommand;
import com.beyond.bycontract.contract.domain.model.Contract;
import com.beyond.bycontract.contract.domain.repository.ContractRepository;
import com.beyond.bycontract.user.application.service.UserService;
import com.beyond.bycontract.user.domain.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractService {

	private final ContractRepository repository;
	private final UserService userService;

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
				command.value()
		);

		Contract savedContract = repository.create(contract);

		User author = userService.getUserById(savedContract.getIdAuthor()).orElseThrow(() -> new RuntimeException("No user found"));

		return new ContractResponse(
				savedContract.getId(),
				savedContract.getName(),
				savedContract.getContractStatus(),
				author.getFirstName(),
				author.getLastName(),
				"GROUPE BEYOND" //To change
		);

	}
}
