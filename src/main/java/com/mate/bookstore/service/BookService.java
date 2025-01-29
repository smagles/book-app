package com.mate.bookstore.service;

import com.mate.bookstore.dto.BookDto;
import com.mate.bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);
}
