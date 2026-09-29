package com.beyond.bycontract.contract.application.dto;

import com.beyond.bycontract.contract.domain.model.ContractStatus;
import com.beyond.bycontract.contract.domain.model.ContractType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateContractCommand (
        UUID idContract,
         UUID idRequester,
         String name,
         UUID idCompany,
         String bodyText,
         JsonNode bodyJson,
         LocalDate effectiveDate,
        LocalDate expirationDate,
         ContractType contractType,
         ContractStatus contractStatus,
        BigDecimal value,
        Boolean autoRenew
){
}
