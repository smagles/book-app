package com.mate.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrderRequestDto(@NotBlank(message = "Shipping address is required")
                                    @Size(min = 5, max = 255, message =
                                            "Address must be between 5 and 255 characters")
                                    String shippingAddress) {

}
