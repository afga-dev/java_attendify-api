package com.attendify.attendify_api.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Authentication response containing access and refresh JWT tokens.")
@Builder
public record AuthResponseDTO(
        @Schema(
            description = "JWT access token used to authorize API requests.",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        @JsonProperty("access_token")
        String accessToken,

        @Schema(
            description = "JWT refresh token used to obtain a new access token.",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        @JsonProperty("refresh_token")
        String refreshToken) {
}
