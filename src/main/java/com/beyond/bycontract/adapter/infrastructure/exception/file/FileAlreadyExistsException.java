package com.beyond.bycontract.adapter.infrastructure.exception.file;

public class FileAlreadyExistsException extends RuntimeException {
    public FileAlreadyExistsException(String message) {
        super(message);
    }
    public FileAlreadyExistsException(String message, Throwable cause) { super(message, cause); }
    public FileAlreadyExistsException(Throwable cause) { super(cause); }
}
