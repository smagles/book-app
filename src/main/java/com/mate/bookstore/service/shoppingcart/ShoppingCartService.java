package com.mate.bookstore.service.shoppingcart;

import com.mate.bookstore.dto.shoppingcart.AddToCartRequestDto;
import com.mate.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.mate.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import com.mate.bookstore.model.ShoppingCart;
import com.mate.bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(User user);

    ShoppingCartDto addBookToShoppingCart(User user,
                                          AddToCartRequestDto addToCartRequestDto);

    ShoppingCartDto updateBookQuantity(Long cartItemId,
                                       User user,
                                       UpdateCartItemRequestDto updateCartItemRequestDto);

    void deleteFromShoppingCart(Long cartItemId, User user);

    ShoppingCart findShoppingCartByUser(User user);

    void clearShoppingCart(ShoppingCart shoppingCart);
}
