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

                return ResponseEntity.status(status).body(errorResponse);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentialsException(
                        BadCredentialsException ex,
                        HttpServletRequest request) {
                var status = HttpStatus.UNAUTHORIZED;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage());

                return ResponseEntity.status(status).body(errorResponse);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
                        IllegalArgumentException ex,
                        HttpServletRequest request) {
                var status = HttpStatus.CONFLICT;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage());

                return ResponseEntity.status(status).body(errorResponse);
        }

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFoundException(
                        NotFoundException ex,
                        HttpServletRequest request) {
                var status = HttpStatus.NOT_FOUND;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage());

                return ResponseEntity.status(status).body(errorResponse);
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ErrorResponse> handleBadRequestException(
                        NotFoundException ex,
                        HttpServletRequest request) {
                var status = HttpStatus.BAD_REQUEST;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage());

                return ResponseEntity.status(status).body(errorResponse);
        }

        @ExceptionHandler(DuplicateException.class)
        public ResponseEntity<ErrorResponse> handleDuplicateException(
                        NotFoundException ex,
                        HttpServletRequest request) {
                var status = HttpStatus.CONFLICT;
                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getMessage());

                return ResponseEntity.status(status).body(errorResponse);
        }
}
