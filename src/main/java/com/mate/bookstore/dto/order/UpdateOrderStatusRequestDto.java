package com.mate.bookstore.dto.order;

import com.mate.bookstore.model.OrderStatus;
import com.mate.bookstore.validation.EnumValidator;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDto(@NotNull(message = "Order status is required")
                                          @EnumValidator(enumClass = OrderStatus.class,
                                                  message = "Invalid order status")
                                          String status) {
}
