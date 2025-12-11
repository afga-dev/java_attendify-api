package com.attendify.attendify_api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Schema(description = "Request body for user authentication.")
@Builder
public record LoginRequestDTO(
        @Schema(
            description = "User email used for authentication.",
            example = "afga.work.contact@gmail.com"
        )
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(
            description = "User password.",
            example = "4fg4$d3v"
        )
        @NotBlank(message = "Password is required")
        String password) {
}
