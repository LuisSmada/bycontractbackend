package com.beyond.bycontract.company.domain.exception;

import java.util.UUID;

public class CompanyNotFoundException extends RuntimeException {

	public CompanyNotFoundException(String message) {
		super(message);
	}

	public CompanyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompanyNotFoundException(Throwable cause) {
		super(cause);
	}

	public CompanyNotFoundException(UUID id) {
		super("Template with id " + id + " not found");
	}
}
