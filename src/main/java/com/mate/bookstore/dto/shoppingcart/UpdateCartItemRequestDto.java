package com.mate.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;

public record UpdateCartItemRequestDto(@Min(value = 1,
        message = "Quantity must be at least 1") int quantity) {
}
