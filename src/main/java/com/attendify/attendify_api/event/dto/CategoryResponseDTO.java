package com.attendify.attendify_api.event.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Detailed category response.")
@Builder
public record CategoryResponseDTO(
        @Schema(
            description = "Unique identifier of the category.",
            example = "1"
        )
        Long id,

        @Schema(
            description = "Name of the category.",
            example = "Technology"
        )
        String name,

        @Schema(
            description = "Description of the category.",
            example = "Events related to software, hardware, and technology innovation"
        )
        String description) {
}
