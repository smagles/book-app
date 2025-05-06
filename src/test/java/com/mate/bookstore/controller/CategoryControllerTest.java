package com.mate.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.bookstore.dto.category.CategoryDto;
import com.mate.bookstore.dto.category.CreateCategoryRequestDto;
import com.mate.bookstore.dto.category.UpdateCategoryRequestDto;
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
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
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
                    new ClassPathResource("database/add-default-categories.sql")
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
                    new ClassPathResource("database/remove-default-categories.sql")
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Creating a category with valid data should succeed")
    @Sql(scripts = "classpath:database/delete-programming-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequestDto_Success() throws Exception {
        // Given
        CreateCategoryRequestDto validCreateRequest = new CreateCategoryRequestDto(
                "Programming",
                "Books about programming languages and technologies"
        );

        CategoryDto expected = new CategoryDto();
        expected.setName(validCreateRequest.name());
        expected.setDescription(validCreateRequest.description());

        String jsonRequest = objectMapper.writeValueAsString(validCreateRequest);

        // When
        MvcResult result = mockMvc.perform(
                        post("/api/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Creating a category with invalid input should return 400 Bad Request")
    void createCategory_InvalidRequest_ShouldReturnBadRequest() throws Exception {
        CreateCategoryRequestDto invalidRequest = new CreateCategoryRequestDto(
                "",
                "Some description"
        );

        String jsonRequest = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(post("/api/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Get category with non-existing ID should return 404 Not Found")
    void getCategory_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Given
        Long nonExistingId = 999L;

        // When & Then
        mockMvc.perform(
                        get("/api/categories/{id}", nonExistingId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Updating a category with valid data should succeed")
    void updateCategory_ValidRequest_ShouldUpdateCategory() throws Exception {
        // Given
        Long updateId = 1L;

        UpdateCategoryRequestDto requestDto = new UpdateCategoryRequestDto(
                "Updated Name", "Updated Description"
        );

        CategoryDto expected = new CategoryDto();
        expected.setId(updateId);
        expected.setName(requestDto.name());
        expected.setDescription(requestDto.description());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(put("/api/categories/{id}", updateId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }


    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("DELETE category should delete category by ID and return 204 No Content")
    void deleteCategory_WithExistingId_ShouldReturnNoContent() throws Exception {
        // Given
        Long existingCategoryId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/categories/{id}", existingCategoryId))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Deleting a category with a non-existing ID should return 404 Not Found")
    void deleteCategory_NonExistingId_ShouldReturnNotFound() throws Exception {
        // Given
        Long nonExistingId = 999L;

        // When & Then
        mockMvc.perform(delete("/api/categories/{id}", nonExistingId))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Retrieving all categories should return a page of categories")
    void getAllCategories_ShouldReturnPageOfCategories() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").isNumber())
                .andExpect(jsonPath("$.totalPages").isNumber());
    }
}
