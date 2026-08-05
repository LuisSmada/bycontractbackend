package com.beyond.bycontract.company.application.dto;

public record CreateCompanyCommand(
		String name,
		String siret,
		String address
) {
}
