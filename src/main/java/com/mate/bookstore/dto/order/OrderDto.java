package com.mate.bookstore.dto.order;

import com.mate.bookstore.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @NotNull(message = "ID is required")
    private Long id;
    @NotNull(message = "User ID is required")
    private Long userId;
    private Set<OrderItemDto> orderItems;
    @NotNull(message = "Order date cannot be null")
    private LocalDateTime orderDate;
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal total;
    @NotNull(message = "Order status is required")
    private OrderStatus status;
}
