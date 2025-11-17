package com.attendify.attendify_api.shared.exception;

public class DuplicateException extends RuntimeException {
    public DuplicateException(String message) {
        super(message);
    }
}
