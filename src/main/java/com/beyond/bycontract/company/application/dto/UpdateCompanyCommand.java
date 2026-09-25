package com.beyond.bycontract.company.application.dto;

import java.util.UUID;

public record UpdateCompanyCommand(
        UUID idCompany,
        UUID idRequester,
        String name,
        String siret,
        String address,
        MainContactDto mainContact
) {
    public record MainContactDto(
            String firstName,
            String lastName,
            String email,
            String phone
    ) {
    }
}
