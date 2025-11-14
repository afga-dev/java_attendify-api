package com.attendify.attendify_api.event.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategorySimpleDTO {
    private Long id;
    private String name;
}
