package com.mate.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record UpdateBookRequestDto(
        @NotBlank(message = "Title is required") String title,
        @NotBlank(message = "Author is required") String author,
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive") BigDecimal price,
        String description,
        String coverImage
) {}
