package com.attendify.attendify_api.user.dto;

import java.util.Set;

import com.attendify.attendify_api.user.entity.enums.Role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Public summary information of a user.")
@Builder
public record UserSummaryDTO(
        @Schema(
            description = "Unique identifier of the user.",
            example = "1"
        )
        Long id,

        @Schema(
            description = "Email of the user.",
            example = "afga.work.contact@gmail.com"
        )
        String email,

        @Schema(
            description = "Roles assigned to the user.",
            example = "[\"ADMIN\"]"
        )
        Set<Role> roles) {
}
