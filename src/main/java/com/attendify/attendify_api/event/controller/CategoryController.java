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

import com.attendify.attendify_api.event.dto.CategoryRequestDTO;
import com.attendify.attendify_api.event.dto.CategoryResponseDTO;
import com.attendify.attendify_api.event.dto.CategorySimpleDTO;
import com.attendify.attendify_api.event.service.CategoryService;
import com.attendify.attendify_api.shared.dto.PageResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Categories",
    description = "Operations for category management and classification."
)
@RestController
@RequestMapping("/attendify/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
        summary = "Create a new category (ADMIN/MANAGER)",
        description = "Creates a new category. The category name must be unique."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "409", description = "Category name already exists")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO dto) {
        CategoryResponseDTO created = categoryService.create(dto);

        // Returns 201 Created with URI pointing to new resource
        return ResponseEntity.created(URI.create("/attendify/v1/categories/" + created.id())).body(created);
    }

    @Operation(
        summary = "Update a category (ADMIN/MANAGER)",
        description = "Updates an existing category. The authenticated user must be the owner or have force update permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Not owner or insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "409", description = "Category name already exists")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO dto) {
        CategoryResponseDTO updated = categoryService.update(id, dto);

        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Soft-delete a category (ADMIN/MANAGER)",
        description = "Soft-deletes a category. The user must be the owner or have force delete permission."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category soft-deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Category is already deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Not owner or insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);

        // No content returned after successful deletion
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Restore a soft-deleted category (ADMIN)",
        description = "Restores a previously soft-deleted category."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category restored successfully"),
            @ApiResponse(responseCode = "400", description = "Category is not deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('CATEGORY_RESTORE')")
    public ResponseEntity<Void> restoreCategory(@PathVariable Long id) {
        categoryService.restore(id);

        // No content returned after successful restoration
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Get category by ID",
        description = "Retrieves an active category by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @Operation(
        summary = "Get all active categories",
        description = "Returns a paginated list of all active categories."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<PageResponseDTO<CategorySimpleDTO>> getAllCategories(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(categoryService.findAll(pageable));
    }

    @Operation(
        summary = "Get all soft-deleted categories (ADMIN)",
        description = "Returns a paginated list of all soft-deleted categories."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deleted categories retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/deleted")
    @PreAuthorize("hasAuthority('CATEGORY_READ_DELETED')")
    public ResponseEntity<PageResponseDTO<CategorySimpleDTO>> getAllCategoriesDeleted(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(categoryService.findAllDeleted(pageable));
    }

    @Operation(
        summary = "Get all categories including soft-deleted (ADMIN)",
        description = "Returns a paginated list of all categories, including soft-deleted ones."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/with-deleted")
    @PreAuthorize("hasAuthority('CATEGORY_READ_WITH_DELETED')")
    public ResponseEntity<PageResponseDTO<CategorySimpleDTO>> getAllCategoriesWithDeleted(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(categoryService.findAllWithDeleted(pageable));
    }
}
