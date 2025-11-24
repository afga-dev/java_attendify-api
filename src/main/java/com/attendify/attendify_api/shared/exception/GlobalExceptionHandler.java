package com.attendify.attendify_api.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException ex) {
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

                return ResponseEntity.status(status).body(errorResponse);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentialsException(
                        BadCredentialsException ex) {
                var status = HttpStatus.UNAUTHORIZED;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage());

                return ResponseEntity.status(status).body(errorResponse);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
                        IllegalArgumentException ex) {
                var status = HttpStatus.CONFLICT;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage());

                return ResponseEntity.status(status).body(errorResponse);
        }

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFoundException(
                        NotFoundException ex) {
                var status = HttpStatus.NOT_FOUND;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage());

                return ResponseEntity.status(status).body(errorResponse);
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ErrorResponse> handleBadRequestException(
                        BadRequestException ex) {
                var status = HttpStatus.BAD_REQUEST;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage());

                return ResponseEntity.status(status).body(errorResponse);
        }

        @ExceptionHandler(DuplicateException.class)
        public ResponseEntity<ErrorResponse> handleDuplicateException(
                        DuplicateException ex) {
                var status = HttpStatus.CONFLICT;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage());

                return ResponseEntity.status(status).body(errorResponse);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
                        HttpMessageNotReadableException ex) {

                String message = "Malformed JSON request";

                if (ex.getCause() instanceof com.fasterxml.jackson.core.JsonParseException) {
                        message = "Invalid JSON structure";
                } else if (ex.getCause() instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
                        message = "Invalid value provided for a field";
                }

                var status = HttpStatus.BAD_REQUEST;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                message);

                return ResponseEntity.status(status).body(errorResponse);
        }

        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<ErrorResponse> handleIllegalStateException(
                        IllegalStateException ex) {
                var status = HttpStatus.CONFLICT;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage());

                return ResponseEntity.status(status).body(errorResponse);
        }
}
