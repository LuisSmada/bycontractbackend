package com.beyond.bycontract.template.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TemplateResponse(
		UUID id,
		String name,
		AuthorDto author,
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
