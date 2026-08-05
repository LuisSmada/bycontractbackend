package com.beyond.bycontract.user.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotFoundException(Throwable cause) {
		super(cause);
	}

	public UserNotFoundException(UUID id) {
		super("User with id " + id + " not found");
	}
}
