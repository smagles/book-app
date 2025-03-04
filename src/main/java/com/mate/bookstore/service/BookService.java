package com.mate.bookstore.service;

import com.mate.bookstore.dto.BookDto;
import com.mate.bookstore.dto.BookSearchParametersDto;
import com.mate.bookstore.dto.CreateBookRequestDto;
import com.mate.bookstore.dto.UpdateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto update(Long id, UpdateBookRequestDto requestDto);

    List<BookDto> searchBooks(BookSearchParametersDto searchParameters,
                              Pageable pageable);
}
