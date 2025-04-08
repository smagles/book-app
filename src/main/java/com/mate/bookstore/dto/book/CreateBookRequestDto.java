package com.mate.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CreateBookRequestDto(
        @NotBlank(message = "Title is required")
        @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
        String title,
        @NotBlank(message = "Author is required")
        @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
        String author,
        @NotBlank(message = "isbn is required")
        String isbn,
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price,
        String description,
        String coverImage
) {
}
