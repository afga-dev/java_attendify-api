package com.attendify.attendify_api.event.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.attendify.attendify_api.event.entity.enums.EventLocation;
import com.attendify.attendify_api.event.entity.enums.EventStatus;
import com.attendify.attendify_api.user.dto.UserSummaryDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Response body representing a full event with its relationships.")
@Builder
public record EventResponseDTO(
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
            description = "Detailed description of the event.",
            example = "A hands-on workshop covering Spring Boot fundamentals"
        )
        String description,

        @Schema(
            description = "Date and time when the event starts.",
            example = "2025-01-01T09:00:00"
        )
        LocalDateTime startDate,

        @Schema(
            description = "Date and time when the event ends.",
            example = "2025-01-01T17:00:00"
        )
        LocalDateTime endDate,

        @Schema(
            description = "Location where the event is held.",
            example = "ONLINE"
        )
        EventLocation location,

        @Schema(
            description = "Maximum number of attendees allowed.",
            example = "50"
        )
        Integer capacity,

        @Schema(
            description = "Current status of the event.",
            example = "PUBLISHED"
        )
        EventStatus status,

        @Schema(
            description = "Users currently registered for the event."
        )
        Set<UserSummaryDTO> registeredUsers,

        @Schema(
            description = "Categories assigned to the event."
        )
        Set<CategorySimpleDTO> categories) {
}
