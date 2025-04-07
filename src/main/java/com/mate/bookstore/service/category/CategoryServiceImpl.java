package com.mate.bookstore.service.category;

import com.mate.bookstore.dto.category.CategoryDto;
import com.mate.bookstore.dto.category.CreateCategoryRequestDto;
import com.mate.bookstore.dto.category.UpdateCategoryRequestDto;
import com.mate.bookstore.exception.EntityNotFoundException;
import com.mate.bookstore.mapper.CategoryMapper;
import com.mate.bookstore.model.Category;
import com.mate.bookstore.repository.category.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(findById(id));
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto update(Long id, UpdateCategoryRequestDto categoryDto) {
        Category category = findById(id);
        return categoryMapper.toDto(categoryMapper.toEntity(categoryDto, category));
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        categoryRepository.deleteById(id);
    }

    private Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Category not found with id: " + id));
    }
}
