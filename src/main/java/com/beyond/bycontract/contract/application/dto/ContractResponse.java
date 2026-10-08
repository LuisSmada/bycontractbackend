package com.beyond.bycontract.contract.application.dto;

import com.beyond.bycontract.contract.domain.model.ContractStatus;
import com.beyond.bycontract.contract.domain.model.ContractType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


public record ContractResponse(
		UUID id,
		String name,
		ContractStatus status,
		ContractType type,
		AuthorDto author,
		CompanyDto company,
		LocalDateTime createdAt,
		LocalDateTime modifiedAt,
		LocalDate expirationDate
) {
	public record AuthorDto(
			UUID id,
			String firstName,
			String lastName
	) {
	}

	public record CompanyDto(
			UUID id,
			String name
	) {
	}
}
