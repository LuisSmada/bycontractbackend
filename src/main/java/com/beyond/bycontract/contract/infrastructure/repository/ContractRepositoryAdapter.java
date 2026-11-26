package com.beyond.bycontract.contract.infrastructure.repository;

import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
import com.beyond.bycontract.company.infrastructure.entity.ParticularStakeholderEntity;
import com.beyond.bycontract.contract.domain.model.Contract;
import com.beyond.bycontract.contract.domain.repository.ContractRepository;
import com.beyond.bycontract.contract.infrastructure.entity.ContractContentEntity;
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
	public final ContractPersistenceMapper mapper;

	@Override
	public Contract create(Contract contract) {
		ContractEntity savedEntity = jpaRepository.save(mapper.domainToEntity(contract));
		return mapper.entityToDomain(savedEntity);
	}

	@Override
	public List<Contract> getAllContracts() {
		List<ContractEntity> contractEntities = jpaRepository.findAll();
		return contractEntities.stream().map(mapper::entityToDomain).toList();
	}

	@Override
	public List<Contract> getAllContractsByIdCreator(UUID idCreator) {
		List<ContractEntity> contractEntities = jpaRepository.findAllByIdCreator(idCreator);
		return contractEntities.stream().map(mapper::entityToDomain).toList();
	}

	@Override
	public Optional<Contract> getContractById(UUID id) {
		return jpaRepository.findById(id).map(mapper::entityToDomain);
	}

	@Override
	public Contract updateContract(Contract contract) {
		ContractEntity contractEntity = jpaRepository.findById(contract.getId()).orElseThrow(() -> new RuntimeException("Contract not found with id: " + contract.getId()));

		mapper.updateEntityFromDomain(contract, contractEntity);

		ContractEntity savedEntity = jpaRepository.save(contractEntity);
		return mapper.entityToDomain(savedEntity);

	}

	@Override
	public void deleteContractById(UUID id) {
		jpaRepository.deleteById(id);
	}

}
