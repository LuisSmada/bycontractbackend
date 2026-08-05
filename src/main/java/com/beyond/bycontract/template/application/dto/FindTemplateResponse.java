package com.beyond.bycontract.template.application.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.UUID;

public record FindTemplateResponse(
		UUID id,
		String name,
		String authorName,
		JsonNode body,
		String variablesDefinition,
		LocalDateTime createdAt,
		LocalDateTime modifiedAt
) {
}
