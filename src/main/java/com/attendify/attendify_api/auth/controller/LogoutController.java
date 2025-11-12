package com.attendify.attendify_api.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendify.attendify_api.auth.service.LogoutService;
import com.attendify.attendify_api.shared.exception.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/attendify/v1/auth")
@RequiredArgsConstructor
public class LogoutController {
        private final LogoutService logoutService;

        @PostMapping("/logout")
        public ResponseEntity<?> logout(HttpServletRequest request) {
                var result = logoutService.logout(request);
                switch (result) {
                        case SUCCESS -> {
                                SecurityContextHolder.clearContext();
                                return ResponseEntity.ok().body("Logout successful");
                        }
                        case TOKEN_NOT_FOUND -> {
                                return buildResponse(HttpStatus.NOT_FOUND,
                                                "No active session was found for the provided token");
                        }
                        case ALREADY_REVOKED -> {
                                return buildResponse(HttpStatus.CONFLICT,
                                                "The token has already been revoked or expired");
                        }
                        case INVALID_HEADER -> {
                                return buildResponse(HttpStatus.BAD_REQUEST,
                                                "Missing or malformed Authorization header");
                        }
                }
                return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
        }

        private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
                ErrorResponse body = ErrorResponse.of(status.value(), status.getReasonPhrase(), message);
                return ResponseEntity.status(status).body(body);
        }
}
