package com.attendify.attendify_api.auth.dto;

import java.util.Set;

import com.attendify.attendify_api.shared.validation.Sanitize;
import com.attendify.attendify_api.user.entity.enums.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Schema(description = "Request body for admin-created user registration with custom roles.")
@Builder
public record RegisterAdminRequestDTO(
        @Schema(
            description = "Email of the new user account.",
            example = "manager.email@example.com"
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
        String password,

        @Schema(
            description = "Roles assigned to the user.",
            example = "[\"MANAGER\"]"
        )
        @NotNull
        Set<Role> roles) {
}
