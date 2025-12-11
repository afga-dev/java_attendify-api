package com.attendify.attendify_api.auth.dto;

import com.attendify.attendify_api.shared.validation.Sanitize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Schema(description = "Request body for standard user self-registration.")
@Builder
public record RegisterRequestDTO(
        @Schema(
            description = "Email of the new user account.",
            example = "user.email@example.com"
        )
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Sanitize
        String email,

        @Schema(
            description = "Initial password for the new user.",
            example = "StrongPassword123!"
        )
        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Sanitize
        String password) {
}
