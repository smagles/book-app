package com.mate.bookstore.service.book;

import com.mate.bookstore.dto.book.BookDto;
import com.mate.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.mate.bookstore.dto.book.BookSearchParametersDto;
import com.mate.bookstore.dto.book.CreateBookRequestDto;
import com.mate.bookstore.dto.book.UpdateBookRequestDto;
import com.mate.bookstore.model.Book;
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

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable);

    Book getBookById(Long id);
}
