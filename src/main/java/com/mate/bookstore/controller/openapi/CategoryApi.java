package com.mate.bookstore.controller.openapi;

import com.mate.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.mate.bookstore.dto.category.CategoryDto;
import com.mate.bookstore.dto.category.CreateCategoryRequestDto;
import com.mate.bookstore.dto.category.UpdateCategoryRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Category API", description = "Endpoints for managing book categories")
public interface CategoryApi {

    @Operation(summary = "Create category", description = "Creates a new book category")
    @ApiResponse(responseCode = "201", description = "Category created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CategoryDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "403", description = "Access denied (admin only)")
    CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto requestDto);

    @Operation(summary = "Get all categories",
            description = "Returns a paginated list of all categories")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class))))
    Page<CategoryDto> getAll(@Parameter(description = "Pagination info") Pageable pageable);

    @Operation(summary = "Get category by ID", description = "Returns category details by ID")
    @ApiResponse(responseCode = "200", description = "Category found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CategoryDto.class)))
    @ApiResponse(responseCode = "404", description = "Category not found")
    CategoryDto getCategory(@Parameter(description = "ID of the category") @PathVariable Long id);

    @Operation(summary = "Update category", description = "Updates an existing category by ID")
    @ApiResponse(responseCode = "200", description = "Category updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CategoryDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @ApiResponse(responseCode = "403", description = "Access denied (admin only)")
    CategoryDto updateCategory(@Parameter(description = "ID of the category") @PathVariable Long id,
                               @RequestBody @Valid UpdateCategoryRequestDto requestDto);

    @Operation(summary = "Delete category", description = "Deletes a category by ID")
    @ApiResponse(responseCode = "204", description = "Category deleted")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @ApiResponse(responseCode = "403", description = "Access denied (admin only)")
    void deleteCategory(@Parameter(description = "ID of the category") @PathVariable Long id);

    @Operation(summary = "Get books by category ID",
            description = "Returns books that belong to a specific category")
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = BookDtoWithoutCategoryIds.class))))
    @ApiResponse(responseCode = "404", description = "Category not found")
    Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @Parameter(description = "ID of the category") @PathVariable Long id,
            @Parameter(description = "Pagination info") Pageable pageable);
}
