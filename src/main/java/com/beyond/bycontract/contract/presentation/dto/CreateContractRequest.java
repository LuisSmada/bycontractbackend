package com.beyond.bycontract.contract.presentation.dto;

import com.beyond.bycontract.contract.application.dto.CreateContractCommand;
import com.beyond.bycontract.contract.domain.model.ContractType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateContractRequest(
		@NotBlank String name,
		@NotNull UUID idAuthor,
		@NotNull UUID idCompany,
		UUID idTemplate,
		@NotNull String bodyText,
		@NotNull JsonNode bodyJson,
		@NotNull LocalDate effectiveDate,
		LocalDate expirationDate,
		@NotNull ContractType contractType,
		BigDecimal value,
		Boolean autoRenew


) {

	public CreateContractCommand toCommand() {
		return new CreateContractCommand(
				this.name(),
				this.idAuthor(),
				this.idCompany(),
				this.idTemplate(),
				this.bodyText,
				this.bodyJson,
				this.effectiveDate(),
				this.expirationDate(),
				this.contractType(),
				this.value(),
				this.autoRenew != null ? this.autoRenew : false
		);
	}
}
