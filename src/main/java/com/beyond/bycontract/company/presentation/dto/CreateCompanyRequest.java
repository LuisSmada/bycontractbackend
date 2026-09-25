package com.beyond.bycontract.company.presentation.dto;


import com.beyond.bycontract.company.application.dto.CreateCompanyCommand;

import java.util.UUID;

public record CreateCompanyRequest(
		String name,
		String siret,
		String address,
		ContactRequest mainContact
) {

	public record ContactRequest(
			String firstName,
			String lastName,
			String email,
			String phone
	) {
	}


	public CreateCompanyCommand toCommand(UUID idCreator) {
		return new CreateCompanyCommand(
				this.name(),
				idCreator,
				this.siret(),
				this.address(),
				new CreateCompanyCommand.ContactCommand(
						this.mainContact().firstName(),
						this.mainContact().lastName(),
						this.mainContact().email(),
						this.mainContact().phone()
				)
		);
	}
}
