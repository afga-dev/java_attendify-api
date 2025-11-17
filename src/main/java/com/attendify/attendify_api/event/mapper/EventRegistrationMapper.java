package com.attendify.attendify_api.event.mapper;

import org.springframework.stereotype.Component;

import com.attendify.attendify_api.event.dto.EventRegistrationResponseDTO;
import com.attendify.attendify_api.event.model.Event;
import com.attendify.attendify_api.event.model.EventRegistration;
import com.attendify.attendify_api.user.model.User;

@Component
public class EventRegistrationMapper {
    public EventRegistration toEntity(User user, Event event) {
        return EventRegistration.builder()
                .user(user)
                .event(event)
                .checkedIn(false)
                .build();
    }

    public EventRegistrationResponseDTO toResponse(EventRegistration eventRegistration) {
        return EventRegistrationResponseDTO.builder()
                .id(eventRegistration.getId())
                .userId(eventRegistration.getUser().getId())
                .eventId(eventRegistration.getEvent().getId())
                .checkedIn(eventRegistration.getCheckedIn())
                .createdAt(eventRegistration.getCreatedAt())
                .build();
    }
}
