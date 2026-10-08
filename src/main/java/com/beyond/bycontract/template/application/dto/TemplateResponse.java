package com.beyond.bycontract.template.application.dto;

import com.beyond.bycontract.template.domain.model.TemplateStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TemplateResponse(
		UUID id,
		String name,
		AuthorDto author,
		TemplateStatus status,
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
