package com.attendify.attendify_api.shared.exception;

import java.time.Instant;

public record ErrorResponse(
        Instant timestap,
        int status,
        String error,
        String message) {
    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(Instant.now(), status, error, message);
    }
}
