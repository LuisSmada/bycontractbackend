package com.beyond.bycontract.file.domain.exception;

public class FileAlreadyExistsException extends RuntimeException {
	public FileAlreadyExistsException(String message) {
		super(message);
	}

	public FileAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileAlreadyExistsException(Throwable cause) {
		super(cause);
	}
}
