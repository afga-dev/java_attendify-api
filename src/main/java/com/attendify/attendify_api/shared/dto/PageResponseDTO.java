package com.attendify.attendify_api.shared.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Generic paginated response wrapper used across the API.")
@Builder
public record PageResponseDTO<T>(
        @Schema(
            description = "List of items in the current page."
        )
        List<T> items,

        @Schema(
            description = "Current page number (0-based index).",
            example = "0"
        )
        int page,

        @Schema(
            description = "Number of items per page.",
            example = "10"
        )
        int size,

        @Schema(
            description = "Total number of items across all pages.",
            example = "50"
        )
        long totalItems,

        @Schema(
            description = "Total number of available pages.",
            example = "5"
        )
        long totalPages,

        @Schema(
            description = "Indicates whether this is the last available page.",
            example = "false"
        )
        boolean isLast) {
}
