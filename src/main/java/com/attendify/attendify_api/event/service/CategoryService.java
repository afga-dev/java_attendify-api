package com.attendify.attendify_api.event.service;

import java.util.List;

import com.attendify.attendify_api.event.dto.CategoryRequestDTO;
import com.attendify.attendify_api.event.dto.CategoryResponseDTO;
import com.attendify.attendify_api.event.dto.CategorySimpleDTO;

public interface CategoryService {
    CategoryResponseDTO create(CategoryRequestDTO dto);

    CategoryResponseDTO update(Long id, CategoryRequestDTO dto);

    void delete(Long id);

    CategoryResponseDTO findById(Long id);

    List<CategorySimpleDTO> findAll();
}
