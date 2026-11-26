package com.beyond.bycontract.contract.presentation.dto;

import com.beyond.bycontract.contract.application.dto.UpdateContractCommand;
import com.beyond.bycontract.contract.domain.model.ContractStatus;
import com.beyond.bycontract.contract.domain.model.ContractType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateContractRequest(
        @NotBlank String name,
        UUID idCompany,
        UUID idParticularStakeholder,
        UUID idTemplate,
        @NotNull String bodyText,
        @NotNull JsonNode bodyJson,
        @NotNull LocalDate effectiveDate,
        LocalDate expirationDate,
        @NotNull ContractType contractType,
        @NotNull ContractStatus status,
        BigDecimal value,
        Boolean autoRenew
) {

    @AssertTrue(message = "Vous devez fournir soit idCompany, soit idParticularStakeholder (un seul des deux).")
    public boolean isStakeholderValid() {
        return (idCompany != null) ^ (idParticularStakeholder != null);
    }

    public UpdateContractCommand toCommand(UUID idContract, UUID idRequester) {
        return new UpdateContractCommand(
                idContract,
                idRequester,
                this.name(),
                this.idCompany(),
                this.idParticularStakeholder(),
                this.bodyText(),
                this.bodyJson(),
                this.effectiveDate(),
                this.expirationDate(),
                this.contractType(),
                this.status(),
                this.value(),
                this.autoRenew()
        );
    }
}
