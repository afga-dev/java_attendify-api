package com.attendify.attendify_api.auth.dto;

import com.attendify.attendify_api.shared.validation.Sanitize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Schema(description = "Request body to change the authenticated user's password.")
@Builder
public record ChangePasswordRequestDTO(
        @Schema(
            description = "Current user password.",
            example = "StrongPassword123!"
        )
        @NotBlank(message = "Current password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Sanitize
        String currentPassword,

        @Schema(
            description = "New password to replace the current one.",
            example = "NewStrongPassword123!"
        )
        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Sanitize
        String newPassword) {
}
