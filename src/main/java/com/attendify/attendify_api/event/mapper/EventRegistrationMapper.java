package com.attendify.attendify_api.event.mapper;

import org.springframework.stereotype.Component;

import com.attendify.attendify_api.event.dto.EventRegistrationResponseDTO;
import com.attendify.attendify_api.event.model.EventRegistration;

@Component
public class EventRegistrationMapper {
    public EventRegistrationResponseDTO toResponse(EventRegistration event) {
        return EventRegistrationResponseDTO.builder()
                .id(event.getId())
                .userId(event.getUser().getId())
                .eventId(event.getEvent().getId())
                .checkedIn(event.getCheckedIn())
                .createdAt(event.getCreatedAt())
                .build();
    }
}
