package com.beyond.bycontract.company.presentation.dto;

import com.beyond.bycontract.company.application.dto.CreateCompanyCommand;

public record CreateCompanyRequest(
		String name,
		String siret,
		String address
) {
	public CreateCompanyCommand toCommand() {
		return new CreateCompanyCommand(
				this.name(),
				this.siret(),
				this.address()
		);
	}
}
