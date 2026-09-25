package com.beyond.bycontract.company.application.dto;


import java.util.UUID;

public record CreateCompanyCommand(
		String name,
		UUID idCreator,
		String siret,
		String address,
		ContactCommand mainContact
) {
	public record ContactCommand(
			String firstName,
			String lastName,
			String email,
			String phone
	) {
	}
}

