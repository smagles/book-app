package com.mate.bookstore.service.shopingcart;

import com.mate.bookstore.dto.shoppingcart.AddToCartRequestDto;
import com.mate.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.mate.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import com.mate.bookstore.exception.EntityNotFoundException;
import com.mate.bookstore.mapper.ShoppingCartMapper;
import com.mate.bookstore.model.CartItem;
import com.mate.bookstore.model.ShoppingCart;
import com.mate.bookstore.model.User;
import com.mate.bookstore.repository.shopingcart.ShoppingCartRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemService cartItemService;

    @Override
    public ShoppingCartDto getShoppingCart(User user) {
        ShoppingCart shoppingCart = findShoppingCart(user);
        if (shoppingCart == null) {
            throw new EntityNotFoundException("Shopping cart not found");
        }
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto addBookToShoppingCart(User user,
                                                 AddToCartRequestDto addToCartRequestDto) {
        ShoppingCart shoppingCart = findShoppingCart(user);
        if (shoppingCart == null) {
            shoppingCart = createShoppingCart(user);
        }
        CartItem cartItem = cartItemService.createCartItem(addToCartRequestDto.bookId(),
                addToCartRequestDto.quantity(), shoppingCart);
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        cartItems.add(cartItem);
        shoppingCart.setCartItems(cartItems);
        shoppingCart = shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateBookQuantity(Long cartItemId, User user,
                                              UpdateCartItemRequestDto updateCartItemRequestDto) {
        CartItem cartItem = cartItemService.getCartItem(cartItemId);
        cartItem = cartItemService
                .updateCartItem(cartItem, user, updateCartItemRequestDto.quantity());

        return shoppingCartMapper.toDto(cartItem.getShoppingCart());
    }

    @Override
    @Transactional
    public void deleteFromShoppingCart(Long cartItemId, User user) {
        cartItemService.deleteCartItem(cartItemId, user);
    }

    private ShoppingCart findShoppingCart(User user) {
        return shoppingCartRepository.findByUserId(user.getId())
                .orElse(null);
    }

    private ShoppingCart createShoppingCart(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        cart.setCartItems(new HashSet<>());
        return shoppingCartRepository.save(cart);
    }

}
