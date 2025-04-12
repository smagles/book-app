package com.mate.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;

public record UpdateCartItemRequestDto(@Min(1) int quantity) {
}
