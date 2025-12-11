package com.attendify.attendify_api.event.entity.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Defines where the event takes place.")
public enum EventLocation {
    ONLINE,
    PRESENTIAL,
    HYBRID
}
