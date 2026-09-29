package com.beyond.bycontract.contract.presentation.dto;

import com.beyond.bycontract.contract.application.dto.UpdateContractCommand;
import com.beyond.bycontract.contract.domain.model.ContractStatus;
import com.beyond.bycontract.contract.domain.model.ContractType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateContractRequest(
        @NotBlank String name,
        @NotNull UUID idCompany,
        @NotNull String bodyText,
        @NotNull JsonNode bodyJson,
        @NotNull LocalDate effectiveDate,
        LocalDate expirationDate,
        @NotNull ContractType contractType,
        @NotNull ContractStatus contractStatus,
        BigDecimal value,
        Boolean autoRenew
) {
    public UpdateContractCommand toCommand(UUID idContract, UUID idRequester) {
        return new UpdateContractCommand(
                idContract,
                idRequester,
                this.name(),
                this.idCompany(),
                this.bodyText(),
                this.bodyJson(),
                this.effectiveDate(),
                this.expirationDate(),
                this.contractType(),
                this.contractStatus(),
                this.value(),
                this.autoRenew()
        );
    }
}
