package com.beyond.bycontract.adapter.infrastructure.exception.folder;

public class FolderAlreadyExistsException extends RuntimeException {
    public FolderAlreadyExistsException(String message) {
        super(message);
    }
    public FolderAlreadyExistsException(String message, Throwable cause) { super(message, cause); }
    public FolderAlreadyExistsException(Throwable cause) { super(cause); }
}
