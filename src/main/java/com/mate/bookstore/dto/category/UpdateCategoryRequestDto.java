package com.mate.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryRequestDto(@NotBlank(message = "Name is required") String name,
                                       String description) {
}
