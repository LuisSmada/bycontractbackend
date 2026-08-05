package com.beyond.bycontract.template.domain.exception;

import java.util.UUID;

public class TemplateNotFoundException extends RuntimeException {
	public TemplateNotFoundException(String message) {
		super(message);
	}

	public TemplateNotFoundException(UUID id) {
		super("Template with id " + id + " not found");
	}
}
