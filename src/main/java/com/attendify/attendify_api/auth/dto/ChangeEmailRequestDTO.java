package com.attendify.attendify_api.auth.dto;

import com.attendify.attendify_api.shared.validation.Sanitize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Schema(description = "Request body to change the authenticated user's email.")
@Builder
public record ChangeEmailRequestDTO(
        @Schema(
            description = "Current user email.",
            example = "user.email@example.com"
        )
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Sanitize
        String currentEmail,

        @Schema(
            description = "New email to replace the current one.",
            example = "new.user.email@example.com"
        )
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Sanitize
        String newEmail) {
}
