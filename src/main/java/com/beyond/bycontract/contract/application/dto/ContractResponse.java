package com.beyond.bycontract.contract.application.dto;

import com.beyond.bycontract.contract.domain.model.ContractStatus;

import java.util.UUID;


public record ContractResponse(
		UUID id,
		String name,
		ContractStatus contractStatus,
		String authorFirstName,
		String authorLastName,
		String companyName
) {
}
