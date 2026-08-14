package com.beyond.bycontract.contract.infrastructure.repository;

import com.beyond.bycontract.contract.domain.model.Contract;
import com.beyond.bycontract.contract.domain.repository.ContractRepository;
import com.beyond.bycontract.contract.infrastructure.entity.ContractEntity;
import com.beyond.bycontract.contract.infrastructure.mapper.ContractPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ContractRepositoryAdapter implements ContractRepository {

	public final JpaContractRepository jpaRepository;

	@Override
	public Contract create(Contract contract) {
		ContractEntity savedEntity = jpaRepository.save(ContractPersistenceMapper.toEntity(contract));
		return ContractPersistenceMapper.reconstituteDomain(savedEntity);
	}

	@Override
	public List<Contract> getAllContracts() {
		List<ContractEntity> contractEntities = jpaRepository.findAll();
		return contractEntities.stream().map(ContractPersistenceMapper::reconstituteDomain).toList();
	}

	@Override
	public Optional<Contract> getContractById(UUID id) {
		return jpaRepository.findById(id).map(ContractPersistenceMapper::reconstituteDomain);
	}


}
