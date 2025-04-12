package com.mate.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    @NotNull(message = "ID is required")
    private Long id;
    @NotNull(message = "Book ID is required")
    private Long bookId;
    @NotBlank(message = "Book title is required")
    private String bookTitle;
    @Min(1)
    private int quantity;

}
