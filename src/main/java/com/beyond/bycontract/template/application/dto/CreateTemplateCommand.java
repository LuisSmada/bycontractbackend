package com.beyond.bycontract.template.application.dto;

import com.beyond.bycontract.template.domain.model.TemplateStatus;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record CreateTemplateCommand(
		String name,
		UUID idAuthor,
		TemplateStatus status,
		JsonNode body
) {
}
