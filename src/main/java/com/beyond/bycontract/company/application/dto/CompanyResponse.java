package com.beyond.bycontract.company.application.dto;

import java.util.UUID;

public record CompanyResponse(
		UUID id,
		String name,
		String siret,
		String address,
		MainContactResponse mainContact
) {
	public record MainContactResponse(
			String firstName,
			String lastName,
			String email,
			String phone
	) {
	}
}
