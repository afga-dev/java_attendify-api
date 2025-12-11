package com.attendify.attendify_api.event.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Simplified category representation for listings.")
@Builder
public record CategorySimpleDTO(
        @Schema(
            description = "Unique identifier of the category.",
            example = "1"
        )
        Long id,

        @Schema(
            description = "Name of the category.",
            example = "Technology"
        )
        String name) {
}
