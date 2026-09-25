package com.beyond.bycontract.company.presentation.dto;

import com.beyond.bycontract.company.application.dto.UpdateCompanyCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UpdateCompanyRequest(
        @NotBlank String name,
        String siret,
        String address,
        // @Valid cascade la validation vers le sous-objet s'il est présent
        @Valid MainContactDto mainContact
) {

    public record MainContactDto(
            String firstName,
            String lastName,
            @Email(message = "Email should be valid")
            String email,
            String phone
    ) {
    }

    public UpdateCompanyCommand toCommand(UUID idCompany, UUID idRequester) {
        return new UpdateCompanyCommand(
                idCompany,
                idRequester,
                this.name(),
                this.siret(),
                this.address(),
                this.mainContact() != null ? new UpdateCompanyCommand.MainContactDto(
                        this.mainContact().firstName(),
                        this.mainContact().lastName(),
                        this.mainContact().email(),
                        this.mainContact().phone()
                ) : null
        );
    }

}
