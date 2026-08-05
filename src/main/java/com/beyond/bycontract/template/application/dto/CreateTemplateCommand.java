package com.beyond.bycontract.template.application.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record CreateTemplateCommand(
		String name,
		JsonNode body,
		UUID idAuthor
) {
}
