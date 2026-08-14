package com.beyond.bycontract.contract.domain.repository;

import com.beyond.bycontract.contract.domain.model.Contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContractRepository {

	Contract create(Contract contract);

	List<Contract> getAllContracts();

	Optional<Contract> getContractById(UUID id);
}
