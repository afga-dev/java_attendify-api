package com.attendify.attendify_api.event.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventRegistrationResponseDTO {
    private Long id;
    private Long userId;
    private Long eventId;
    private Boolean checkedIn;
    private LocalDateTime createdAt;
}
