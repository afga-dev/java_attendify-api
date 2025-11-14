package com.attendify.attendify_api.event.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventRegistrationRequestDTO {
    @NotNull(message = "Event ID is required")
    private Long eventId;
}
