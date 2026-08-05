package com.beyond.bycontract.contract.infrastructure.repository;

import com.beyond.bycontract.contract.domain.model.Contract;
import com.beyond.bycontract.contract.domain.repository.ContractRepository;
import com.beyond.bycontract.contract.infrastructure.entity.ContractEntity;
import com.beyond.bycontract.contract.infrastructure.mapper.ContractPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ContractRepositoryAdapter implements ContractRepository {

	public final JpaContractRepository jpaRepository;

	@Override
	public Contract create(Contract contract) {
		ContractEntity savedEntity = jpaRepository.save(ContractPersistenceMapper.toEntity(contract));
		return ContractPersistenceMapper.reconstituteDomain(savedEntity);
	}
}
