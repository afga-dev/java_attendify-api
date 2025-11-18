package com.attendify.attendify_api.event.dto;

import com.attendify.attendify_api.shared.validation.Sanitize;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDTO {
    @NotBlank(message = "Name is required")
    @Sanitize
    private String name;

    @NotBlank(message = "Description is required")
    @Sanitize
    private String description;
}
