package com.attendify.attendify_api.event.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(description = "Request body to register the authenticated user to an event.")
@Builder
public record EventRegistrationRequestDTO(
        @Schema(
            description = "Unique identifier of the event registration.",
            example = "1"
        )
        @NotNull(message = "Event ID is required")
        Long eventId) {
}
