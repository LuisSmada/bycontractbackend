package com.beyond.bycontract.contract.application.dto;

import com.beyond.bycontract.contract.domain.model.ContractStatus;
import com.beyond.bycontract.contract.domain.model.ContractType;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record FindContractResponse(
		UUID id,
		Boolean autoRenew,
		ContractStatus status,
		ContractType contractType,
		LocalDateTime createdAt,
		LocalDateTime modifiedAt,
		LocalDate effectiveDate,
		LocalDate expirationDate,
		String name,
		BigDecimal value,
		AuthorDto author,
		CompanyDto company,
		ContractContentDto content,
		UUID idTemplate
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

	public record ContractContentDto(
			JsonNode bodyJson,
			String plainText,
			String signedPdfUrl,
			LocalDateTime modifiedAt
	) {
	}

	public record TemplateDto(
			UUID id,
			String name
	) {
	}
}
