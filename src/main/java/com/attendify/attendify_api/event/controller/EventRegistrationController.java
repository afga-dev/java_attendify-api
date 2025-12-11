package com.attendify.attendify_api.event.controller;

import java.net.URI;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendify.attendify_api.event.dto.EventRegistrationAdminRequestDTO;
import com.attendify.attendify_api.event.dto.EventRegistrationRequestDTO;
import com.attendify.attendify_api.event.dto.EventRegistrationResponseDTO;
import com.attendify.attendify_api.event.service.EventRegistrationService;
import com.attendify.attendify_api.shared.dto.PageResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Event Registrations",
    description = "Operations related to event registrations, check-in, and registration management."
)
@RestController
@RequestMapping("/attendify/v1/registrations")
@RequiredArgsConstructor
public class EventRegistrationController {
    private final EventRegistrationService eventRegistrationService;

    @Operation(
        summary = "Register user to an event",
        description = "Registers the authenticated user to an event if capacity is available and the event has not ended."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registration created successfully"),
            @ApiResponse(responseCode = "400", description = "Already registered, event full, or event already ended"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Event or user not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<EventRegistrationResponseDTO> createRegistration(@Valid @RequestBody EventRegistrationRequestDTO dto) {
        EventRegistrationResponseDTO created = eventRegistrationService.create(dto);

        // Returns 201 Created with URI pointing to new resource
        return ResponseEntity.created(URI.create("/attendify/v1/registrations/" + created.id())).body(created);
    }

    @Operation(
        summary = "Force register a user to an event (ADMIN/MANAGER)",
        description = "Registers a user to an event if capacity is available and the event has not ended. The authenticated user must be the owner of the event or have force create permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation or business rule error"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Event or user not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/force")
    @PreAuthorize("hasAuthority('EVENT_REGISTRATION_CREATE')")
    public ResponseEntity<EventRegistrationResponseDTO> forceRegisterUser(
            @Valid @RequestBody EventRegistrationAdminRequestDTO dto
    ) {
        EventRegistrationResponseDTO created = eventRegistrationService.createByForce(dto);

        // Returns 201 Created with URI pointing to new resource
        return ResponseEntity.created(URI.create("/attendify/v1/registrations/" + created.id())).body(created);
    }

    @Operation(
        summary = "Cancel an event registration",
        description = "Soft deletes a registration. The authenticated user must be the owner or have force delete permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registration cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Already deleted, cannot cancel after check-in or event started"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Not owner and missing force delete permission"),
            @ApiResponse(responseCode = "404", description = "Registration not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_REGISTRATION_DELETE')")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        eventRegistrationService.delete(id);

        // No content returned after successful deletion
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Restore a soft-deleted registration (ADMIN)",
        description = "Restores a previously soft-deleted event registration."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Registration restored successfully"),
            @ApiResponse(responseCode = "400", description = "Registration is not deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Registration not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('EVENT_REGISTRATION_RESTORE')")
    public ResponseEntity<Void> restoreRegistration(@PathVariable Long id) {
        eventRegistrationService.restore(id);

        // No content returned after successful restoration
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Check-in to an event (ADMIN/MANAGER)",
        description = "Marks a user as checked in. The authenticated user must be the owner of the event or have force check-in permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Check-in successful"),
            @ApiResponse(responseCode = "400", description = "Already checked in or event has not started"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Not owner and missing force check-in permission"),
            @ApiResponse(responseCode = "404", description = "Registration not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}/check-in")
    @PreAuthorize("hasAuthority('EVENT_REGISTRATION_CHECKIN')")
    public ResponseEntity<EventRegistrationResponseDTO> checkIn(@PathVariable Long id) {
        EventRegistrationResponseDTO checkIn = eventRegistrationService.checkIn(id);

        return ResponseEntity.ok(checkIn);
    }

    @Operation(
        summary = "Get users registered for an event (ADMIN/MANAGER)",
        description = "Returns a paginated list of users registered to the given event."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registrations retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Not owner and missing force check-in permission"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{eventId}")
    @PreAuthorize("hasAuthority('EVENT_REGISTRATION_READ_BY_EVENT')")
    public ResponseEntity<PageResponseDTO<EventRegistrationResponseDTO>> getUsersByEvent(
            @PathVariable Long eventId,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(eventRegistrationService.getUsersByEvent(eventId, pageable));
    }

    @Operation(
        summary = "Get my registered events",
        description = "Returns a paginated list of events the authenticated user is registered for."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registrations retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<PageResponseDTO<EventRegistrationResponseDTO>> getMyEvents(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(eventRegistrationService.getMyEvents(pageable));
    }

    @Operation(
        summary = "Get all active event registrations",
        description = "Returns a paginated list of all active event registrations."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    @PreAuthorize("hasAuthority('EVENT_REGISTRATION_FORCE_READ')")
    public ResponseEntity<PageResponseDTO<EventRegistrationResponseDTO>> getAllEventRegistrations(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(eventRegistrationService.findAll(pageable));
    }

    @Operation(
        summary = "Get soft-deleted event registrations (ADMIN)",
        description = "Returns a paginated list of all soft-deleted event registrations."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deleted registrations retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('EVENT_REGISTRATION_READ_DELETED')")
    public ResponseEntity<PageResponseDTO<EventRegistrationResponseDTO>> getAllEventRegistrationsDeleted(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(eventRegistrationService.findAllDeleted(pageable));
    }

    @Operation(
        summary = "Get all event registrations including soft-deleted (ADMIN)",
        description = "Returns a paginated list of all event registrations, including soft-deleted ones."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registrations retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/with-deleted")
    @PreAuthorize("hasAuthority('EVENT_REGISTRATION_READ_WITH_DELETED')")
    public ResponseEntity<PageResponseDTO<EventRegistrationResponseDTO>> getAllEventRegistrationsIncludingDeleted(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(eventRegistrationService.findAllWithDeleted(pageable));
    }
}
