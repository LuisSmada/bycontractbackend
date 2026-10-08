package com.beyond.bycontract.contract.infrastructure.repository;

import com.beyond.bycontract.company.infrastructure.entity.CompanyEntity;
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

	@Override
	public Contract updateContract(Contract contract) {
		ContractEntity contractEntity = jpaRepository.findById(contract.getId()).orElseThrow(() -> new RuntimeException("Contract not found with id: " + contract.getId()));

		contractEntity.setName(contract.getName());
		contractEntity.setContractType(contract.getContractType());
		contractEntity.setContractStatus(contract.getContractStatus());
		contractEntity.setAutoRenew(contract.getAutoRenew());
		contractEntity.setEffectiveDate(contract.getEffectiveDate());
		contractEntity.setExpirationDate(contract.getExpirationDate());
		contractEntity.setValue(contract.getValue());

		if (contract.getIdCompany() != null) {
			CompanyEntity companyEntity = new CompanyEntity();
			companyEntity.setId(contract.getIdCompany());
			contractEntity.setCompany(companyEntity);
		}

		ContractContentEntity contentEntity = contractEntity.getContent();
		if (contentEntity != null && contract.getContractContent() != null) {
			contentEntity.setBody(contract.getContractContent().getBody());
			contentEntity.setPlainText(contract.getContractContent().getPlainText());
			contentEntity.setModifiedAt(contract.getContractContent().getModifiedAt());
		}

		ContractEntity savedEntity = jpaRepository.save(contractEntity);

		return ContractPersistenceMapper.reconstituteDomain(savedEntity);

	}

	@Override
	public void deleteContractById(UUID id) {
		jpaRepository.deleteById(id);
	}

}
