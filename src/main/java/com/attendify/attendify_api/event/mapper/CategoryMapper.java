package com.attendify.attendify_api.event.mapper;

import org.springframework.stereotype.Component;

import com.attendify.attendify_api.event.dto.CategoryRequestDTO;
import com.attendify.attendify_api.event.dto.CategoryResponseDTO;
import com.attendify.attendify_api.event.dto.CategorySimpleDTO;
import com.attendify.attendify_api.event.model.Category;

@Component
public class CategoryMapper {
    public Category toEntity(CategoryRequestDTO dto) {
        return Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public void updateEntity(Category category, CategoryRequestDTO dto) {
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
    }

    public CategoryResponseDTO toResponse(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public CategorySimpleDTO toSimple(Category category) {
        return CategorySimpleDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
