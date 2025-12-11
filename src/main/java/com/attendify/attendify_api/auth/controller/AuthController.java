package com.attendify.attendify_api.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendify.attendify_api.auth.dto.AuthResponseDTO;
import com.attendify.attendify_api.auth.dto.ChangeEmailRequestDTO;
import com.attendify.attendify_api.auth.dto.ChangePasswordRequestDTO;
import com.attendify.attendify_api.auth.dto.LoginRequestDTO;
import com.attendify.attendify_api.auth.dto.RegisterAdminRequestDTO;
import com.attendify.attendify_api.auth.dto.RegisterRequestDTO;
import com.attendify.attendify_api.auth.service.AuthService;
import com.attendify.attendify_api.shared.dto.MessageResponseDTO;
import com.attendify.attendify_api.shared.security.SecurityConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Authentication",
    description = "Endpoints for user authentication, authorization, and account security."
)
@RestController
@RequestMapping("/attendify/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with the default USER role and returns access and refresh tokens."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping("/register-user")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(dto));
    }

    @Operation(
        summary = "Register a new user (ADMIN)",
        description = "Creates a user with explicitly assigned roles and returns access and refresh tokens."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('USER_FORCE_CREATE')")
    public ResponseEntity<AuthResponseDTO> registerByAdmin(@Valid @RequestBody RegisterAdminRequestDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.registerByAdmin(dto));
    }

    @Operation(
        summary = "User login",
        description = "Authenticates user credentials and returns new access and refresh tokens."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @Operation(
        summary = "Refresh access token",
        description = "Generates a new access token using a valid refresh token from the Authorization header."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "400", description = "Missing or malformed authorization header"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDTO> refresh(HttpServletRequest request) {
        String authHeader = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);

        AuthResponseDTO authResponse = authService.refreshToken(authHeader);

        return ResponseEntity.ok(authResponse);
    }

    @Operation(
        summary = "Logout user",
        description = "Revokes the current refresh token and invalidates the session."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "404", description = "Token not found"),
            @ApiResponse(responseCode = "409", description = "Token already revoked")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDTO> logout(HttpServletRequest request) {
        String authHeader = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);

        var result = authService.logout(authHeader);

        return switch (result) {
            case SUCCESS -> {
                // Clear any security context stored in the current request
                SecurityContextHolder.clearContext();
                yield ResponseEntity.ok(new MessageResponseDTO("Logout successful"));
            }
            case TOKEN_NOT_FOUND -> buildMessageResponse(HttpStatus.NOT_FOUND,
                    "No active session was found for the provided token");
            case ALREADY_REVOKED -> buildMessageResponse(HttpStatus.CONFLICT,
                    "The token has already been revoked or expired");
        };
    }

    @Operation(
        summary = "Change password",
        description = "Updates the authenticated user's password and revokes all active tokens."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "Password validation conflict")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/password")
    public ResponseEntity<MessageResponseDTO> changePassword(@Valid @RequestBody ChangePasswordRequestDTO dto) {
        var result = authService.changePassword(dto);

        return switch (result) {
            case SUCCESS -> {
                // Forces the user to re-authenticate after the password change
                SecurityContextHolder.clearContext();
                yield ResponseEntity.ok().body(new MessageResponseDTO("Change password successful"));
            }
            case OLD_DO_NOT_MATCH -> buildMessageResponse(HttpStatus.CONFLICT,
                    "Current password is incorrect");

            case NEW_MATCH_OLD -> buildMessageResponse(HttpStatus.CONFLICT,
                    "New password must be different");

            case INVALID_HEADER -> buildMessageResponse(HttpStatus.BAD_REQUEST,
                    "Missing or malformed authorization header");
        };
    }

    @Operation(
        summary = "Change email",
        description = "Updates the authenticated user's email and revokes all active tokens."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email changed successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "Email validation conflict")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/email")
    public ResponseEntity<MessageResponseDTO> changeEmail(@Valid @RequestBody ChangeEmailRequestDTO dto) {
        var result = authService.changeEmail(dto);

        return switch (result) {
            case SUCCESS -> {
                // Forces the user to re-authenticate after the email change
                SecurityContextHolder.clearContext();
                yield ResponseEntity.ok().body(new MessageResponseDTO("Change email successful"));
            }
            case OLD_DO_NOT_MATCH -> buildMessageResponse(HttpStatus.CONFLICT,
                    "Current email is incorrect");
            case NEW_MATCH_OLD -> buildMessageResponse(HttpStatus.CONFLICT,
                    "New email must be different");
            case ALREADY_EXISTS -> buildMessageResponse(HttpStatus.CONFLICT,
                    "Email is already in use");
            case INVALID_HEADER -> buildMessageResponse(HttpStatus.BAD_REQUEST,
                    "Missing or malformed authorization header");
        };
    }

    // Centralized helper to keep error responses consistent
    private ResponseEntity<MessageResponseDTO> buildMessageResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new MessageResponseDTO(message));
    }
}
