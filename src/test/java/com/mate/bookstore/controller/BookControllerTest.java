package com.mate.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.bookstore.dto.book.BookDto;
import com.mate.bookstore.dto.book.CreateBookRequestDto;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired DataSource datasource,
                          @Autowired WebApplicationContext applicationContext) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(datasource);

        try (Connection connection = datasource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/add-default-books.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource datasource) {
        teardown(datasource);
    }

    @SneakyThrows
    private static void teardown(DataSource datasource) {
        try (Connection connection = datasource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/remove-default-books.sql")
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/delete-java-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        // Given
        CreateBookRequestDto validCreateRequest = new CreateBookRequestDto(
                "Effective Java",
                "Joshua Bloch",
                "978-0134685994",
                new BigDecimal("39.99"),
                "Definitive guide to Java programming language",
                "effective_java_3rd_edition.jpg"
        );

        BookDto expected = BookDto.builder()
                .title(validCreateRequest.title())
                .author(validCreateRequest.author())
                .price(validCreateRequest.price())
                .isbn(validCreateRequest.isbn())
                .description(validCreateRequest.description())
                .coverImage(validCreateRequest.coverImage())
                .build();

        String jsonRequest = objectMapper.writeValueAsString(validCreateRequest);

        // When
        MvcResult result = mockMvc.perform(
                        post("/api/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/delete-java-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_NoValidRequestDto_ShouldThrowDuplicateIsbnException() throws Exception {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Effective Java",
                "Joshua Bloch",
                "978-0134685991",
                new BigDecimal("39.99"),
                "Definitive guide to Java programming language",
                "effective_java_3rd_edition.jpg"
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        mockMvc.perform(post("/api/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isConflict());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get category with non-existing ID should return 404 Not Found")
    void getBook_WithNonExistingId_ShouldThrowNotFoundException() throws Exception {
        // Given
        Long nonExistingId = 999L;

        // When & Then
        mockMvc.perform(get("/api/books/{id}", nonExistingId)
        ).andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("DELETE book should delete book by ID and return 204 No Content")
    void deleteBook_WithExistingId_ShouldReturnNoContent() throws Exception {
        // Given
        Long existingCategoryId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/books/{id}", existingCategoryId))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get category with non-existing ID should return 404 Not Found")
    void deleteBook_WithNonExistingId_ShouldThrowNotFoundException() throws Exception {
        // Given
        Long nonExistingId = 999L;

        // When & Then
        mockMvc.perform(get("/api/books/{id}", nonExistingId))
                .andExpect(status().isNotFound());

    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get all books should return list of all books")
    void getAll_GivenBooks_ShouldReturnAllBooks() throws Exception {
        //Given
        List<BookDto> expected = List.of(
                BookDto.builder()
                        .id(1L)
                        .title("Effective Java")
                        .author("Joshua Bloch")
                        .isbn("978-0134685991")
                        .price(new BigDecimal("45.99"))
                        .description("Definitive guide to Java programming")
                        .coverImage("effective_java.jpg")
                        .build(),
                BookDto.builder()
                        .id(2L)
                        .title("Clean Code")
                        .author("Robert C. Martin")
                        .isbn("978-0132350884")
                        .price(new BigDecimal("39.99"))
                        .description("Handbook of agile software craftsmanship")
                        .coverImage("clean_code.jpg")
                        .build(),
                BookDto.builder()
                        .id(3L)
                        .title("Design Patterns")
                        .author("Erich Gamma")
                        .isbn("978-0201633610")
                        .price(new BigDecimal("49.99"))
                        .description("Elements of reusable object-oriented software")
                        .coverImage("design_patterns.jpg")
                        .build()
        );

        // When
        MvcResult result = mockMvc.perform(
                        get("/api/books")
                                .contentType(MediaType.APPLICATION_JSON)


                )
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }
}
