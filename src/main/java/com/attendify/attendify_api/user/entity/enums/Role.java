package com.attendify.attendify_api.user.entity.enums;

import java.util.EnumSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "System roles that define user access levels.")
@Getter
@RequiredArgsConstructor
public enum Role {
    @Schema(description = "Standard user with basic access permissions.")
    USER(Set.of(
            Permission.EVENT_REGISTRATION_DELETE)),

    @Schema(description = "Event manager with permissions to create and manage events they own.")
    MANAGER(Set.of(
            Permission.CATEGORY_CREATE,
            Permission.CATEGORY_UPDATE,
            Permission.CATEGORY_DELETE,

            Permission.EVENT_CREATE,
            Permission.EVENT_UPDATE,
            Permission.EVENT_DELETE,

            Permission.EVENT_REGISTRATION_READ_BY_EVENT,
            Permission.EVENT_REGISTRATION_CREATE,
            Permission.EVENT_REGISTRATION_CHECKIN,
            Permission.EVENT_REGISTRATION_DELETE)),

    @Schema(description = "Administrator with full system access.")
    ADMIN(EnumSet.allOf(Permission.class));

    private final Set<Permission> permissions;
}
