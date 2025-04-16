package com.mate.bookstore.dto.order;

import com.mate.bookstore.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDto(@NotNull(message = "Order status is required")
                                       OrderStatus status) {
}
