package com.beyond.bycontract.template.presentation.dto;

import com.beyond.bycontract.template.application.dto.CreateTemplateCommand;
import com.beyond.bycontract.template.domain.model.TemplateStatus;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record CreateTemplateRequest(
		String name,
		TemplateStatus status,
		JsonNode body
) {
	public CreateTemplateCommand toCommand(UUID idAuthor) {
		return new CreateTemplateCommand(
				this.name(),
				idAuthor,
				this.status(),
				this.body()
		);
	}
}
