package com.attendify.attendify_api.user.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendify.attendify_api.shared.dto.PageResponseDTO;
import com.attendify.attendify_api.user.dto.UserSummaryDTO;
import com.attendify.attendify_api.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Users",
    description = "Operations related to user management and administration."
)
@RestController
@RequestMapping("/attendify/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
        summary = "Get my user profile",
        description = "Returns the profile data of the currently authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authenticated user retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    public ResponseEntity<UserSummaryDTO> getMyUser() {
        return ResponseEntity.ok(userService.getMyUser());
    }

    @Operation(
        summary = "Get a user (ADMIN)",
        description = "Retrieves a specific user (including soft-deleted users) by ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_FORCE_READ')")
    public ResponseEntity<UserSummaryDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Operation(
        summary = "Soft-delete my account",
        description = "Soft-deletes the currently authenticated user's account and revokes all active tokens."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User account soft-deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe() {
        userService.delete();

        SecurityContextHolder.clearContext();

        // No content returned after successful deletion
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Delete a user (ADMIN)",
        description = "Soft-deletes a user by ID. Administrators cannot delete their own account using this endpoint."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User soft-deleted successfully"),
            @ApiResponse(responseCode = "400", description = "User already soft-deleted or self-deletion is not allowed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_FORCE_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);

        // No content returned after successful deletion
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Restore soft-deleted user (ADMIN)",
        description = "Restores a previously soft-deleted user by ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User restored successfully"),
            @ApiResponse(responseCode = "400", description = "User is not deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('USER_RESTORE')")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        userService.restore(id);

        // No content returned after successful restoration
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get all active users (ADMIN)",
        description = "Returns a paginated list of all active users."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping()
    @PreAuthorize("hasAuthority('USER_READ_ALL')")
    public ResponseEntity<PageResponseDTO<UserSummaryDTO>> getAll(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @Operation(
        summary = "Get all soft-deleted users (ADMIN)",
        description = "Returns a paginated list of all soft-deleted users."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deleted users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('USER_READ_DELETED')")
    public ResponseEntity<PageResponseDTO<UserSummaryDTO>> getAllDeleted(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(userService.findAllDeleted(pageable));
    }

    @Operation(
        summary = "Get all users including soft-deleted (ADMIN)",
        description = "Returns a paginated list of all users, including soft-deleted ones."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/with-deleted")
    @PreAuthorize("hasAuthority('USER_READ_WITH_DELETED')")
    public ResponseEntity<PageResponseDTO<UserSummaryDTO>> getAllWithDeleted(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(userService.findAllWithDeleted(pageable));
    }
}
