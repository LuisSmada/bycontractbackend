package com.beyond.bycontract.template.application.dto;

import com.beyond.bycontract.template.domain.model.TemplateStatus;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.util.UUID;

public record FindTemplateResponse(
		UUID id,
		String name,
		AuthorDto author,
		TemplateStatus status,
		JsonNode body,
		String variablesDefinition,
		LocalDateTime createdAt,
		LocalDateTime modifiedAt
) {
	public record AuthorDto(
			UUID id,
			String firstName,
			String lastName
	) {
	}
}
