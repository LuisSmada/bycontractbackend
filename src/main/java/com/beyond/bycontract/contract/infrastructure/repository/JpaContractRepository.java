package com.beyond.bycontract.contract.infrastructure.repository;

import com.beyond.bycontract.contract.infrastructure.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaContractRepository extends JpaRepository<ContractEntity, UUID> {
}
