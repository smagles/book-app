package com.mate.bookstore.controller;

import com.mate.bookstore.dto.shoppingcart.AddToCartRequestDto;
import com.mate.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.mate.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import com.mate.bookstore.model.User;
import com.mate.bookstore.service.shoppingcart.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto getCart(@AuthenticationPrincipal User user) {
        return shoppingCartService.getShoppingCart(user);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartDto addBookToShoppingCart(@AuthenticationPrincipal User user,
                                                 @Valid @RequestBody
                                                 AddToCartRequestDto addToCartRequestDto) {
        return shoppingCartService.addBookToShoppingCart(user, addToCartRequestDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto updateQuantity(@PathVariable("cartItemId") Long cartItemId,
                                          @AuthenticationPrincipal User user,
                                          @Valid @RequestBody
                                              UpdateCartItemRequestDto shoppingCartDto) {
        return shoppingCartService.updateBookQuantity(cartItemId, user, shoppingCartDto);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable("cartItemId") Long cartItemId,
                               @AuthenticationPrincipal User user) {
        shoppingCartService.deleteFromShoppingCart(cartItemId, user);
    }
}
