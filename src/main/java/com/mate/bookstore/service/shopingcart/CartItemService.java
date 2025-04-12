package com.mate.bookstore.service.shopingcart;

import com.mate.bookstore.model.CartItem;
import com.mate.bookstore.model.ShoppingCart;
import com.mate.bookstore.model.User;

public interface CartItemService {
    CartItem createCartItem(Long bookId, int quantity, ShoppingCart shoppingCart);

    CartItem getCartItem(Long id);

    CartItem updateCartItem(CartItem cartItem, User user, int quantity);

    void deleteCartItem(Long id, User user);
}
