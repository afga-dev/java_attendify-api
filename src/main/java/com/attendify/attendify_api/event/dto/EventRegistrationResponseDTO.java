package com.attendify.attendify_api.event.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Response body representing a user's registration to an event.")
@Builder
public record EventRegistrationResponseDTO(
        @Schema(
            description = "Unique identifier of the event registration.",
            example = "1"
        )
        Long id,

        @Schema(
            description = "Unique identifier of the registered user.",
            example = "1"
        )
        Long userId,

        @Schema(
            description = "Unique identifier of the associated event.",
            example = "1"
        )
        Long eventId,

        @Schema(
            description = "Indicates whether the user has checked in to the event.",
            example = "false"
        )
        Boolean checkedIn,

        @Schema(
            description = "Timestamp when the registration was created.",
            example = "2026-01-01T12:00:00"
        )
        LocalDateTime createdAt) {
}
