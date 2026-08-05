package com.beyond.bycontract.contract.domain.repository;

import com.beyond.bycontract.contract.domain.model.Contract;

public interface ContractRepository {

	Contract create(Contract contract);
}
