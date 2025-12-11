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

import com.attendify.attendify_api.event.dto.EventFilterDTO;
import com.attendify.attendify_api.event.dto.EventRequestDTO;
import com.attendify.attendify_api.event.dto.EventResponseDTO;
import com.attendify.attendify_api.event.dto.EventSimpleDTO;
import com.attendify.attendify_api.event.service.EventService;
import com.attendify.attendify_api.shared.dto.PageResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Events",
    description = "Operations for event creation, management, and querying."
)
@RestController
@RequestMapping("/attendify/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @Operation(
        summary = "Create a new event (ADMIN/MANAGER)",
        description = "Creates a new event."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Event created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or validation failed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "409", description = "Event conflict or duplicate")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("hasAuthority('EVENT_CREATE')")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRequestDTO dto) {
        EventResponseDTO created = eventService.create(dto);

        // Returns 201 Created with URI pointing to new resource
        return ResponseEntity.created(URI.create("/attendify/v1/events/" + created.id())).body(created);
    }

    @Operation(
        summary = "Update an event (ADMIN/MANAGER)",
        description = "Updates an existing event. The authenticated user must be the owner or have force update permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or invalid update"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Not owner or insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Event not found"),
            @ApiResponse(responseCode = "409", description = "Event conflict or duplicate")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_UPDATE')")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequestDTO dto) {
        EventResponseDTO updated = eventService.update(id, dto);

        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Soft-delete an event (ADMIN/MANAGER)",
        description = "Soft-deletes an event. The authenticated user must be the owner or have force delete permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Event soft-deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Event already deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Not owner or insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EVENT_DELETE')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.delete(id);

        // No content returned after successful deletion
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Restore a soft-deleted event (ADMIN)",
        description = "Restores a previously soft-deleted event."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Event restored successfully"),
            @ApiResponse(responseCode = "400", description = "Event is not deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('EVENT_RESTORE')")
    public ResponseEntity<Void> restoreEvent(@PathVariable Long id) {
        eventService.restore(id);

        // No content returned after successful restoration
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get event by ID",
        description = "Retrieves a single active event by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Event retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @Operation(
        summary = "Get all active events",
        description = "Returns a paginated list of active events using optional filter parameters."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid filter or pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<PageResponseDTO<EventSimpleDTO>> getAllEvents(
            @ParameterObject EventFilterDTO eventFilter,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(eventService.findAll(eventFilter, pageable));
    }

    @Operation(
        summary = "Get all active events by category",
        description = "Returns a paginated list of active events that belong to a specific category."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Category or event not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}/category")
    public ResponseEntity<PageResponseDTO<EventSimpleDTO>> getByCategory(
            @PathVariable Long id,
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(eventService.findByCategory(id, pageable));
    }

    @Operation(
        summary = "Get all soft-deleted events (ADMIN)",
        description = "Returns a paginated list of all soft-deleted events."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deleted events retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('EVENT_READ_DELETED')")
    public ResponseEntity<PageResponseDTO<EventSimpleDTO>> getAllEventsDeleted(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(eventService.findAllDeleted(pageable));
    }

    @Operation(
        summary = "Get all events including soft-deleted (ADMIN)",
        description = "Returns a paginated list of all events, including soft-deleted ones."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/with-deleted")
    @PreAuthorize("hasAuthority('EVENT_READ_WITH_DELETED')")
    public ResponseEntity<PageResponseDTO<EventSimpleDTO>> getAllWithDeleted(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(eventService.findAllWithDeleted(pageable));
    }
}
