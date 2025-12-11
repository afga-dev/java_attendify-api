package com.attendify.attendify_api.event.dto;

import com.attendify.attendify_api.shared.validation.Sanitize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Schema(description = "Request body used to create or update a category.")
@Builder
public record CategoryRequestDTO(
        @Schema(
            description = "Name of the category.",
            example = "Technology"
        )
        @NotBlank(message = "Name is required")
        @Sanitize
        String name,

        @Schema(
            description = "Detailed description of the category.",
            example = "Events related to software, hardware, and technology innovation"
        )
        @NotBlank(message = "Description is required")
        @Sanitize
        String description) {
}
