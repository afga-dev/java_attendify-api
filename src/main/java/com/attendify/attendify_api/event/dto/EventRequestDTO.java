package com.attendify.attendify_api.event.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.attendify.attendify_api.event.entity.enums.EventLocation;
import com.attendify.attendify_api.event.entity.enums.EventStatus;
import com.attendify.attendify_api.shared.validation.Sanitize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(description = "Request body used to create or update an event.")
@Builder
public record EventRequestDTO(
        @Schema(
            description = "Title of the event.",
            example = "Spring Boot Workshop"
        )
        @NotBlank(message = "Title is required")
        @Sanitize
        String title,

        @Schema(
            description = "Detailed description of the event.",
            example = "A hands-on workshop covering Spring Boot fundamentals"
        )
        @NotBlank(message = "Description is required")
        @Sanitize
        String description,

        @Schema(
            description = "Date and time when the event starts.",
            example = "2026-01-01T09:00:00"
        )
        @NotNull(message = "Start date is required")
        LocalDateTime startDate,

        @Schema(
            description = "Date and time when the event ends.",
            example = "2026-01-01T17:00:00"
        )
        @NotNull(message = "End date is required")
        LocalDateTime endDate,

        @Schema(
            description = "Location where the event will be held.",
            example = "ONLINE"
        )
        @NotNull(message = "Location is required")
        EventLocation location,

        @Schema(
            description = "Maximum number of attendees allowed.",
            example = "50"
        )
        @NotNull(message = "Capacity is required")
        Integer capacity,

        @Schema(
            description = "Current status of the event.",
            example = "PUBLISHED"
        )
        @NotNull(message = "Status is required")
        EventStatus status,

        @Schema(
            description = "Set of category IDs associated with the event.",
            example = "[1]"
        )
        @NotEmpty(message = "Event must have at least one category")
        Set<Long> categoryIds) {
}
