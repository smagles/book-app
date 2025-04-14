package com.mate.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddToCartRequestDto(@NotNull(message = "Book ID cannot be null") Long bookId,
                                  @Min(value = 1,
                                          message = "Quantity must be at least 1") int quantity) {
}
