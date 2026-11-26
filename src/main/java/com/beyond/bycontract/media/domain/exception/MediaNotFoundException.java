package com.beyond.bycontract.media.domain.exception;

public class MediaNotFoundException extends RuntimeException {
    public MediaNotFoundException(String key) {
        super("Media not found with key: " + key);
    }
}
