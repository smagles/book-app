package com.mate.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.bookstore.dto.shoppingcart.AddToCartRequestDto;
import com.mate.bookstore.dto.shoppingcart.CartItemDto;
import com.mate.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.mate.bookstore.dto.shoppingcart.UpdateCartItemRequestDto;
import com.mate.bookstore.model.User;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void beforeEach() throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/add-default-shopping-cart.sql")
            );
        }
    }

    @AfterEach
    void afterEach() throws SQLException {
        teardown(dataSource);
    }

    @SneakyThrows
    private static void teardown(DataSource datasource) throws SQLException{
        try (Connection connection = datasource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/remove-default-shopping-cart.sql")
            );
        }
    }

    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    @Test
    @DisplayName("Get cart for authenticated user - should return valid ShoppingCartDto")
    void getCart_ValidUser_ShouldReturnShoppingCartDto() throws Exception {
        // Given
        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("testuser@example.com");

        ShoppingCartDto expected = new ShoppingCartDto(
                1L,
                1L,
                Set.of(new CartItemDto(1L, 1L, "Test Book Title", 2))
        );

        // When
        MvcResult result = mockMvc.perform(get("/api/cart")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(2))
                .andReturn();

        // Then
        ShoppingCartDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());

        EqualsBuilder.reflectionEquals(expected, actual);

    }

    @WithMockUser(username = "nonexistent.user@example.com", roles = {"USER"})
    @Test
    @DisplayName("Get cart with non-existing user should return 404 Not Found")
    void getCart_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Given
        User testUser = new User();
        testUser.setId(999L);
        testUser.setEmail("nonexistent.user@example.com");

        // When & Then
        mockMvc.perform(
                        get("/api/cart")
                                .with(user(testUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    @Test
    @DisplayName("Add book to shopping cart should return 404 Not Found")
    void addBookToShoppingCart_WithNonExistingBookId_ShouldReturnNotFound() throws Exception {
        // Given
        Long bookId = 999L;

        AddToCartRequestDto requestDto = new AddToCartRequestDto(bookId, 2);
        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("testuser@example.com");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When & Then
        mockMvc.perform(post("/api/cart")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());

    }

    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    @Test
    @DisplayName("Add book with invalid quantity - should return 400 Bad Request")
    void addBookToShoppingCart_InvalidQuantity_ShouldReturnBadRequest() throws Exception {
        // Given
        int quantity = -1;

        AddToCartRequestDto requestDto = new AddToCartRequestDto(1L, quantity);

        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("testuser@example.com");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When & Then
        mockMvc.perform(post("/api/cart")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    @Test
    @DisplayName("Update quantity with non-existing cart item ID - should return 404")
    void updateCartItemQuantity_InvalidCartItemId_ShouldReturnNotFound() throws Exception {
        // Given
        int quantity = 2;

        UpdateCartItemRequestDto updateDto = new UpdateCartItemRequestDto(quantity);
        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("testuser@example.com");

        String jsonRequest = objectMapper.writeValueAsString(updateDto);

        // When & Then
        mockMvc.perform(put("/api/cart/cart-items/999")
                        .with(user(testUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    @Test
    @DisplayName("Delete existing cart item - should return 204 No Content")
    void deleteCartItem_ValidCartItemId_ShouldReturnNoContent() throws Exception {
        // Given
        User testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("testuser@example.com");

        // When & Then
        mockMvc.perform(delete("/api/cart/cart-items/1")
                        .with(user(testUser)))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    @Test
    @DisplayName("Add existing book to shopping cart – should increase quantity in ShoppingCartDto")
    void addBookToShoppingCart_ExistingBookInCart_ShouldIncreaseQuantity() throws Exception {
        // Given
        Long bookId = 1L;
        Long userId = 1L;
        int quantity = 2;

        AddToCartRequestDto requestDto = new AddToCartRequestDto(bookId, quantity);
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("testuser@example.com");

        ShoppingCartDto expected = new ShoppingCartDto(
                1L,
                userId,
                Set.of(new CartItemDto(1L, bookId, "Test Book Title", 4)));

        // When
        MvcResult result = mockMvc.perform(post("/api/cart")
                        .with(user(mockUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.cartItems[0].bookId").value(bookId))
                .andExpect(jsonPath("$.cartItems[0].quantity").value(4))
                .andReturn();

        // Then
        ShoppingCartDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ShoppingCartDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());

        EqualsBuilder.reflectionEquals(expected, actual);
    }
}
