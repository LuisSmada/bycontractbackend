package com.beyond.bycontract.contract.application.dto;

import com.beyond.bycontract.contract.domain.model.ContractStatus;

import java.time.LocalDateTime;
import java.util.UUID;


public record ContractResponse(
		UUID id,
		String name,
		ContractStatus status,
		AuthorDto author,
		CompanyDto company,
		LocalDateTime createdAt,
		LocalDateTime modifiedAt
) {
	public record AuthorDto(
			String firstName,
			String lastName
	) {
	}

	public record CompanyDto(
			String name
	) {
	}
}
