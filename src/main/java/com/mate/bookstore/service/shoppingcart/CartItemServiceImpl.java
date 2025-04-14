package com.mate.bookstore.service.shoppingcart;

import com.mate.bookstore.exception.EntityNotFoundException;
import com.mate.bookstore.model.Book;
import com.mate.bookstore.model.CartItem;
import com.mate.bookstore.model.ShoppingCart;
import com.mate.bookstore.model.User;
import com.mate.bookstore.repository.shoppingcart.CartItemRepository;
import com.mate.bookstore.service.book.BookService;
import java.util.Optional;
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
        Optional<CartItem> cartItemExists = getCartItemIfExists(bookId, shoppingCart);

        if (cartItemExists.isPresent()) {
            return updateCartItem(cartItemExists.get().getId(), shoppingCart.getUser(), quantity);
        }

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(quantity);
        cartItem.setShoppingCart(shoppingCart);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(Long id, User user, int quantity) {
        CartItem cartItem = getValidatedCartItem(id, user);
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteCartItem(Long id, User user) {
        cartItemRepository.delete(getValidatedCartItem(id, user));
    }

    private CartItem findCartItemById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException(
                        "CartItem not found with id: " + id));
    }

    private void validateCartItemOwnership(CartItem cartItem, User user) {
        if (!cartItem.getShoppingCart().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Cart item with id " + cartItem.getId()
                    + " doesn't belong to user " + user.getId());
        }
    }

    private CartItem getValidatedCartItem(Long id, User user) {
        CartItem cartItem = findCartItemById(id);
        validateCartItemOwnership(cartItem, user);
        return cartItem;
    }

    private Optional<CartItem> getCartItemIfExists(Long bookId, ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(bookId))
                .findFirst();
    }
}
