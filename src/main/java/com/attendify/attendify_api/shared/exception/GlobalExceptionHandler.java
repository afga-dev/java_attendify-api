package com.attendify.attendify_api.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationExceptions(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                String message = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> error.getDefaultMessage())
                                .findFirst()
                                .orElse("Invalid input data");

                var status = HttpStatus.BAD_REQUEST;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                message);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentialsException(
                        BadCredentialsException ex,
                        HttpServletRequest request) {
                var status = HttpStatus.UNAUTHORIZED;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                "Invalid email or password");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
                        IllegalArgumentException ex,
                        HttpServletRequest request) {
                var status = HttpStatus.CONFLICT;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                "Email is already in use");

                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
}
