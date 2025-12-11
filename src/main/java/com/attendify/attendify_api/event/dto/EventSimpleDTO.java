package com.attendify.attendify_api.event.dto;

import java.time.LocalDateTime;

import com.attendify.attendify_api.event.entity.enums.EventStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Simplified event representation used for listings.")
@Builder
public record EventSimpleDTO(
        @Schema(
            description = "Unique identifier of the event.",
            example = "1"
        )
        Long id,

        @Schema(
            description = "Title of the event.",
            example = "Spring Boot Workshop"
        )
        String title,

        @Schema(
            description = "Date and time when the event starts.",
            example = "2026-01-01T09:00:00"
        )
        LocalDateTime startDate,

        @Schema(
            description = "Date and time when the event ends.",
            example = "2026-01-01T17:00:00"
        )
        LocalDateTime endDate,

        @Schema(
            description = "Current status of the event.",
            example = "PUBLISHED"
        )
        EventStatus status) {
}
