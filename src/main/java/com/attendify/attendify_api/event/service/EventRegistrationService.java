package com.attendify.attendify_api.event.service;

import java.util.List;

import com.attendify.attendify_api.event.dto.EventRegistrationRequestDTO;
import com.attendify.attendify_api.event.dto.EventRegistrationResponseDTO;

public interface EventRegistrationService {
    EventRegistrationResponseDTO create(EventRegistrationRequestDTO dto);

    void delete(Long id);

    EventRegistrationResponseDTO checkIn(Long id);

    List<EventRegistrationResponseDTO> getUserByEvent(Long id);

    List<EventRegistrationResponseDTO> getMyEvents();
}
