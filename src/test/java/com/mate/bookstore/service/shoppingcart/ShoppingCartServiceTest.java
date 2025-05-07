package com.mate.bookstore.service.shoppingcart;

import com.mate.bookstore.dto.shoppingcart.AddToCartRequestDto;
import com.mate.bookstore.dto.shoppingcart.CartItemDto;
import com.mate.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.mate.bookstore.exception.EntityNotFoundException;
import com.mate.bookstore.mapper.ShoppingCartMapper;
import com.mate.bookstore.model.Book;
import com.mate.bookstore.model.CartItem;
import com.mate.bookstore.model.ShoppingCart;
import com.mate.bookstore.model.User;
import com.mate.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.mate.bookstore.service.shoppingcart.item.CartItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemService cartItemService;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private User testUser;
    private Book testBook;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("user@example.com");

        testBook = Book.builder()
                .id(1L)
                .title("Test Book Title")
                .author("Test Author")
                .isbn("978-3-16-148410-0")
                .price(new BigDecimal("19.99"))
                .description("Test book description")
                .coverImage("test_cover.jpg")
                .build();
    }

    @Test
    @DisplayName("Add book to new shopping cart returns correct ShoppingCartDto")
    void addBookToShoppingCart_ValidRequestDto_ReturnsCartShoppingCart() {
        // Given
        AddToCartRequestDto requestDto = new AddToCartRequestDto(testBook.getId(), 2);

        ShoppingCart userCart = new ShoppingCart();
        userCart.setId(1L);
        userCart.setUser(testUser);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(testBook);
        cartItem.setQuantity(requestDto.quantity());
        cartItem.setShoppingCart(userCart);

        userCart.setCartItems(new HashSet<>(Set.of(cartItem)));

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(cartItem.getId());
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setBookId(testBook.getId());
        cartItemDto.setBookTitle(testBook.getTitle());

        ShoppingCartDto expectedDto = new ShoppingCartDto(
                userCart.getId(),
                testUser.getId(),
                Set.of(cartItemDto)
        );

        when(shoppingCartRepository.findByUserId(testUser.getId())).thenReturn(Optional.of(userCart));
        when(cartItemService.createCartItem(requestDto.bookId(),
                requestDto.quantity(), userCart)).thenReturn(cartItem);
        when(shoppingCartRepository.save(userCart)).thenReturn(userCart);
        when(shoppingCartMapper.toDto(userCart)).thenReturn(expectedDto);

        // When
        ShoppingCartDto result = shoppingCartService.addBookToShoppingCart(testUser, requestDto);

        // Then
        assertNotNull(result);
        assertEquals(result, expectedDto);
        assertEquals(1, result.getCartItems().size());

        verify(shoppingCartRepository, times(1)).findByUserId(testUser.getId());
        verify(shoppingCartRepository, times(1)).save(userCart);
        verify(cartItemService).createCartItem(
                requestDto.bookId(),
                requestDto.quantity(),
                userCart);
        verify(shoppingCartMapper).toDto(userCart);
    }

    @Test
    @DisplayName("Get shopping cart for valid user returns correct ShoppingCartDto")
    void getShoppingCart_WithValidUser_ShouldReturnValidShoppingCartDto() {
        // Given
        ShoppingCart userCart = new ShoppingCart();
        userCart.setId(1L);
        userCart.setUser(testUser);

        ShoppingCartDto expectedDto = new ShoppingCartDto(
                userCart.getId(),
                testUser.getId(),
                Set.of()
        );

        when(shoppingCartRepository.findByUserId(testUser.getId())).thenReturn(Optional.of(userCart));
        when(shoppingCartMapper.toDto(userCart)).thenReturn(expectedDto);

        // When
        ShoppingCartDto result = shoppingCartService.getShoppingCart(testUser);

        // Then
        assertNotNull(result);
        assertEquals(result, expectedDto);

        verify(shoppingCartRepository, times(1)).findByUserId(testUser.getId());
        verify(shoppingCartMapper).toDto(userCart);
    }

    @Test
    @DisplayName("Verify the exception was thrown with non existing shopping cart")
    void getShoppingCart_WithNonExistingShoppingCart_ShouldThrowEntityNotFoundException() {
        // Given
        Long userId = 999L;

        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.getShoppingCart(user));

        verify(shoppingCartRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Delete cart item from shopping cart with valid user and item id")
    void deleteFromShoppingCart_ValidItemIdAndUser_DeletesItemSuccessfully() {
        // Given
        Long cartItemId = 1L;

        // When
        shoppingCartService.deleteFromShoppingCart(cartItemId, testUser);

        // Then
        verify(cartItemService).deleteCartItem(cartItemId, testUser);
        verifyNoMoreInteractions(cartItemService);
    }
}
