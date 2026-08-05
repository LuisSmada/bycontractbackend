package com.beyond.bycontract.contract.application.dto;

import com.beyond.bycontract.contract.domain.model.ContractType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateContractCommand(
		String name,
		UUID idAuthor,
		UUID idCompany,
		UUID idTemplate,
		LocalDate effectiveDate,
		LocalDate expirationDate,
		ContractType contractType,
		BigDecimal value,
		Boolean autoRenew
) {
}
