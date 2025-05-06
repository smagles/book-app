package com.mate.bookstore.service.category;

import com.mate.bookstore.dto.category.CategoryDto;
import com.mate.bookstore.dto.category.CreateCategoryRequestDto;
import com.mate.bookstore.dto.category.UpdateCategoryRequestDto;
import com.mate.bookstore.exception.EntityNotFoundException;
import com.mate.bookstore.mapper.CategoryMapper;
import com.mate.bookstore.model.Category;
import com.mate.bookstore.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify findAll() method works")
    void findAll_ValidPageable_ReturnAllCategoriesDto() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fantasy");
        category.setDescription("Fantasy books with magic elements");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryId);
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // When
        Page<CategoryDto> categoryDtos =categoryService.findAll(pageable);

        // Then
        assertThat(categoryDtos).hasSize(1);
        assertThat(categoryDtos.getContent().get(0)).isEqualTo(categoryDto);
        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository,categoryMapper);
    }

    @Test
    @DisplayName("Verify save() method works")
    void save_ValidCreateCategoryRequestDto_ReturnCategoryDto() {
        // Given
        CreateCategoryRequestDto validCreateRequest = new CreateCategoryRequestDto(
                "Science Fiction",
                "Futuristic, space travel, time travel, parallel universes"
        );

        Category category = new Category();
        category.setId(1L);
        category.setName(validCreateRequest.name());
        category.setDescription(validCreateRequest.description());

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        when(categoryMapper.toEntity(validCreateRequest)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        // When
        CategoryDto categoryDtoSaved = categoryService.save(validCreateRequest);

        // Then
        assertThat(categoryDtoSaved).isEqualTo(categoryDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
        verify(categoryMapper).toEntity(validCreateRequest);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify the correct category was returned when category exists")
    void getById_WithValidCategoryId_ShouldReturnValidCategoryDto() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Fantasy");
        category.setDescription("Fantasy books with magic elements");

        CategoryDto expectedCategoryDto = new CategoryDto();
        expectedCategoryDto.setId(categoryId);
        expectedCategoryDto.setName(category.getName());
        expectedCategoryDto.setDescription(category.getDescription());

       when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
       when(categoryMapper.toDto(category)).thenReturn(expectedCategoryDto);

        // When
        CategoryDto actualCategoryDto = categoryService.getById(categoryId);

        // Then
        assertThat(actualCategoryDto)
                .isNotNull()
                .isEqualTo(expectedCategoryDto);

        verify(categoryRepository).findById(categoryId);

    }

    @Test
    @DisplayName("Verify the exception was thrown with non existing category id")
    void get_ById_WithNonExistingCategory_ShouldThrowException() {
        // Given
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        //When
        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class,
                        () -> categoryService.getById(categoryId));

        //Then
        String expected = "Category not found with id: " + categoryId;
        String actual = entityNotFoundException.getMessage();
        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository).findById(categoryId);

    }
    @Test
    @DisplayName("Verify update() works with valid data")
    void update_ValidRequest_ReturnsUpdatedCategoryDto() {
        // Given
        Long categoryId = 1L;
        UpdateCategoryRequestDto updateRequest = new UpdateCategoryRequestDto(
                "Updated category name",
                "Updated category description"
        );

        Category existingCategory = new Category();
        existingCategory.setId(categoryId);
        existingCategory.setName("Original category name");
        existingCategory.setDescription("Original category description");

        Category updatedCategory = new Category();
        updatedCategory.setId(categoryId);
        updatedCategory.setName(updateRequest.name());
        updatedCategory.setDescription(updateRequest.description());

        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setId(categoryId);
        expectedDto.setName(updatedCategory.getName());
        expectedDto.setDescription(updatedCategory.getDescription());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryMapper.toEntity(updateRequest, existingCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expectedDto);

        // When
        CategoryDto result = categoryService.update(categoryId, updateRequest);

        // Then
        assertThat(result).isEqualTo(expectedDto);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper).toEntity(updateRequest, existingCategory);
        verify(categoryMapper).toDto(updatedCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify update() throws exception for non-existent category")
    void update_NonExistentCategory_ThrowsException() {
        // Given
        Long categoryId = 100L;
        UpdateCategoryRequestDto updateRequest = new UpdateCategoryRequestDto(
                "Updated Name",
                "Updated description"
        );

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(categoryId, updateRequest));
        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }
}
