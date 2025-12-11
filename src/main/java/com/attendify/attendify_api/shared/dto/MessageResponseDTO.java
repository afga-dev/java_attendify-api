package com.attendify.attendify_api.shared.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Generic response used to return a simple success or error message.")
public record MessageResponseDTO(
        @Schema(
            description = "Human-readable response message.",
            example = "Operation completed successfully"
        )
        String message) {
}
