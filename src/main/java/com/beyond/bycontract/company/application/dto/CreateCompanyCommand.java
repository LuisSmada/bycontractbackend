package com.beyond.bycontract.company.application.dto;

import java.util.UUID;

public record CreateCompanyCommand(
		String name,
		UUID idCreator,
		String siret,
		String address
) {
}
