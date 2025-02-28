package com.beyond.bycontract.adapter.infrastructure.exception.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(String message, Throwable cause) { super(message, cause); }
    public UserNotFoundException(Throwable cause) { super(cause); }
}
