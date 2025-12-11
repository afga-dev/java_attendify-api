package com.attendify.attendify_api.event.dto;

import com.attendify.attendify_api.event.entity.enums.EventLocation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Filters used to search and narrow down event results.")
@Builder
public record EventFilterDTO(
        @Schema(
            description = "Free text search applied to event name and description."
        )
        String text,

        @Schema(
            description = "If true, returns only upcoming events. If false or null, returns all events."
        )
        Boolean onlyUpcoming,

        @Schema(
            description = "Physical, virtual or hybrid location of the event."
        )
        EventLocation location) {
}
