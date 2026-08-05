package com.beyond.bycontract.template.presentation.dto;

import com.beyond.bycontract.template.application.dto.CreateTemplateCommand;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record CreateTemplateRequest(
		String name,
		JsonNode body,
		UUID idAuthor
) {
	public CreateTemplateCommand toCommand() {
		return new CreateTemplateCommand(
				this.name(),
				this.body(),
				this.idAuthor()
		);
	}
}
