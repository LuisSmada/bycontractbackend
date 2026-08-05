package com.beyond.bycontract.template.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TemplateResponse(
		UUID id,
		String name,
		String authorName,
		LocalDateTime createdAt,
		LocalDateTime modifiedAt
) {
}
