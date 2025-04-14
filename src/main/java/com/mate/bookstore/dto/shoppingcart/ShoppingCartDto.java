package com.mate.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDto {
    @NotNull(message = "ID is required")
    private Long id;
    @NotNull(message = "User ID is required")
    private Long userId;
    private Set<CartItemDto> cartItems;
}
