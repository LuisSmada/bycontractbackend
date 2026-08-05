package com.beyond.bycontract.company.application.dto;

import java.util.UUID;

public record CompanyResponse(
		UUID id,
		String name,
		String siret,
		String address
) {
}
