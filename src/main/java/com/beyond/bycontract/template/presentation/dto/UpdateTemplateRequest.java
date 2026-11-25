package com.beyond.bycontract.template.presentation.dto;

import com.beyond.bycontract.template.application.dto.UpdateTemplateCommand;
import com.beyond.bycontract.template.domain.model.TemplateStatus;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateTemplateRequest(
        String name,
        TemplateStatus status,
        JsonNode body,
        String variablesDefinition
) {
    public UpdateTemplateCommand toCommand(@NotNull UUID idTemplate, @NotNull UUID idRequester) {
        return new UpdateTemplateCommand(
                idTemplate,
                idRequester,
                this.name(),
                this.status(),
                this.body(),
                this.variablesDefinition()
        );
    }
}
