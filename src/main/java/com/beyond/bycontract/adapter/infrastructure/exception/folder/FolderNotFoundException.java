package com.beyond.bycontract.adapter.infrastructure.exception.folder;

public class FolderNotFoundException extends RuntimeException {
    public FolderNotFoundException(String message) {
        super(message);
    }
    public FolderNotFoundException(String message, Throwable cause) { super(message, cause); }
    public FolderNotFoundException(Throwable cause) { super(cause); }
}
