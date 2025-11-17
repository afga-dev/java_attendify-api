package com.attendify.attendify_api.event.service;

import java.util.List;

import com.attendify.attendify_api.event.dto.EventRequestDTO;
import com.attendify.attendify_api.event.dto.EventResponseDTO;
import com.attendify.attendify_api.event.dto.EventSimpleDTO;

public interface EventService {
    EventResponseDTO create(EventRequestDTO dto);

    EventResponseDTO update(Long id, EventRequestDTO dto);

    void delete(Long id);

    EventResponseDTO findById(Long id);

    List<EventSimpleDTO> findAll();

    List<EventSimpleDTO> findByCategory(Long categoryId);
}
