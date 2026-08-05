package com.beyond.bycontract.folder.domain.exception;

public class FolderNotFoundException extends RuntimeException {
	public FolderNotFoundException(String message) {
		super(message);
	}

	public FolderNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public FolderNotFoundException(Throwable cause) {
		super(cause);
	}
}
