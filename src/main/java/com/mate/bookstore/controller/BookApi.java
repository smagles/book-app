package com.mate.bookstore.controller;

import com.mate.bookstore.dto.BookDto;
import com.mate.bookstore.dto.BookSearchParametersDto;
import com.mate.bookstore.dto.CreateBookRequestDto;
import com.mate.bookstore.dto.UpdateBookRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Book API", description = "Endpoints for managing books")
public interface BookApi {

    @Operation(summary = "Get all books", description = "Returns a paginated list of books")
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    List<BookDto> getAll(@Parameter(description = "Pagination information") Pageable pageable);

    @Operation(summary = "Get a book by ID", description = "Returns a book by its ID")
    @ApiResponse(responseCode = "200", description = "Book found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookDto.class)))
    @ApiResponse(responseCode = "404", description = "Book not found")
    BookDto getBookById(@Parameter(description = "ID of the book") @PathVariable Long id);

    @Operation(summary = "Create a new book", description = "Adds a new book to the catalog")
    @ApiResponse(responseCode = "201", description = "Book created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "409", description = "Duplicate ISBN")
    BookDto createBook(@RequestBody CreateBookRequestDto createBookRequestDto);

    @Operation(summary = "Update a book", description = "Updates an existing book by ID")
    @ApiResponse(responseCode = "200", description = "Book updated",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Book not found")
    BookDto updateBook(@Parameter(description = "ID of the book") @PathVariable Long id,
                       @RequestBody @Valid UpdateBookRequestDto updateBookRequestDto);

    @Operation(summary = "Delete a book", description = "Deletes a book by ID")
    @ApiResponse(responseCode = "204", description = "Book deleted")
    @ApiResponse(responseCode = "404", description = "Book not found")
    void deleteBook(@Parameter(description = "ID of the book") @PathVariable Long id);

    @Operation(summary = "Search books", description = "Search for books using various parameters")
    @ApiResponse(responseCode = "200", description = "Books found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookDto.class)))
    List<BookDto> searchBooks(@Parameter(description = "Search parameters")
                              BookSearchParametersDto searchParameters,
                              @Parameter(description = "Pagination information") Pageable pageable);
}
