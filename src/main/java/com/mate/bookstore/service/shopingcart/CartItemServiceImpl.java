package com.mate.bookstore.service.shopingcart;

import com.mate.bookstore.exception.EntityNotFoundException;
import com.mate.bookstore.model.Book;
import com.mate.bookstore.model.CartItem;
import com.mate.bookstore.model.ShoppingCart;
import com.mate.bookstore.model.User;
import com.mate.bookstore.repository.shopingcart.CartItemRepository;
import com.mate.bookstore.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final BookService bookService;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem createCartItem(Long bookId, int quantity, ShoppingCart shoppingCart) {
        Book book = bookService.getBookById(bookId);
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(quantity);
        cartItem.setShoppingCart(shoppingCart);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem getCartItem(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException(
                        "CartItem not found with id: " + id));
    }

    @Override
    public CartItem updateCartItem(CartItem cartItem, User user, int quantity) {
        validateCartItemOwnership(cartItem.getId(), user);
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteCartItem(Long id, User user) {
        validateCartItemOwnership(id, user);
        cartItemRepository.deleteById(id);
    }

    private void validateCartItemOwnership(Long cartItemId, User user) {
        CartItem cartItem = getCartItem(cartItemId);
        if (!cartItem.getShoppingCart().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Cart item with id " + cartItemId
                    + " doesn't belong to user " + user.getId());
        }
    }
}
