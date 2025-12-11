package com.attendify.attendify_api.event.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(description = "Request body for registering a user to an event by an admin.")
public record EventRegistrationAdminRequestDTO(
    @Schema(
        description = "Unique identifier of the event.",
        example = "1"
    )
    @NotNull(message = "Event ID is required")
    Long eventId,

    @Schema(
        description = "Unique identifier of the user.",
        example = "1"
    )
    @NotNull(message = "User ID is required")
    Long userId
) {
}
