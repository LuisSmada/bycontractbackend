package com.beyond.bycontract.adapter.infrastructure.exception.file;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String message) {
        super(message);
    }
    public FileNotFoundException(String message, Throwable cause) { super(message, cause); }
    public FileNotFoundException(Throwable cause) { super(cause); }
}
