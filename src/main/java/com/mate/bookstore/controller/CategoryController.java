package com.mate.bookstore.controller;

import com.mate.bookstore.controller.openapi.CategoryApi;
import com.mate.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.mate.bookstore.dto.category.CategoryDto;
import com.mate.bookstore.dto.category.CreateCategoryRequestDto;
import com.mate.bookstore.dto.category.UpdateCategoryRequestDto;
import com.mate.bookstore.service.book.BookService;
import com.mate.bookstore.service.category.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/categories")
public class CategoryController implements CategoryApi {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto createCategory(@Valid @RequestBody CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAll(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategory(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @Valid @RequestBody UpdateCategoryRequestDto requestDto) {
        return categoryService.update(id, requestDto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable Long id, @PageableDefault(page = 0, size = 10,
            sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        return bookService.findAllByCategoryId(id, pageable);
    }
}
