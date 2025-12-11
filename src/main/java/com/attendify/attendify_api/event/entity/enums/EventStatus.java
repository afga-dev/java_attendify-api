package com.attendify.attendify_api.event.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Defines the event status.")
public enum EventStatus {
    DRAFT,
    PUBLISHED,
    CANCELED,
    COMPLETED
}
