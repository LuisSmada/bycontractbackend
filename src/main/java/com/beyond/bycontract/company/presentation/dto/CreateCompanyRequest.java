package com.beyond.bycontract.company.presentation.dto;

import com.beyond.bycontract.company.application.dto.CreateCompanyCommand;

import java.util.UUID;

public record CreateCompanyRequest(
		String name,
		String siret,
		String address
) {
	public CreateCompanyCommand toCommand(UUID idCreator) {
		return new CreateCompanyCommand(
				this.name(),
				idCreator,
				this.siret(),
				this.address()
		);
	}
}
