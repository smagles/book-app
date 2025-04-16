package com.mate.bookstore.service.shoppingcart;

import com.mate.bookstore.model.CartItem;
import com.mate.bookstore.model.ShoppingCart;
import com.mate.bookstore.model.User;
import java.util.Set;

public interface CartItemService {
    CartItem createCartItem(Long bookId, int quantity, ShoppingCart shoppingCart);

    CartItem updateCartItem(Long id, User user, int quantity);

    void deleteCartItem(Long id, User user);

    void deleteAllCartItems(Set<CartItem> cartItems);
}
