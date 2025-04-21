package com.mate.bookstore.service.book;

import com.mate.bookstore.dto.book.BookDto;
import com.mate.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.mate.bookstore.dto.book.BookSearchParametersDto;
import com.mate.bookstore.dto.book.CreateBookRequestDto;
import com.mate.bookstore.dto.book.UpdateBookRequestDto;
import com.mate.bookstore.exception.DuplicateIsbnException;
import com.mate.bookstore.exception.EntityNotFoundException;
import com.mate.bookstore.mapper.BookMapper;
import com.mate.bookstore.model.Book;
import com.mate.bookstore.repository.book.BookRepository;
import com.mate.bookstore.repository.book.BookSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    @Transactional
    public BookDto save(CreateBookRequestDto createBookRequestDto) {
        validateIsbn(createBookRequestDto.isbn());

        Book book = bookMapper.toModel(createBookRequestDto);
        book = bookRepository.save(book);

        return bookMapper.toBookDto(book);
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toBookDto);
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toBookDto(getBookById(id));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    @Override
    @Transactional
    public BookDto update(Long id, UpdateBookRequestDto updateRequestDto) {
        Book existingBook = getBookById(id);
        bookMapper.updateModel(updateRequestDto, existingBook);
        existingBook = bookRepository.save(existingBook);
        return bookMapper.toBookDto(existingBook);
    }

    @Override
    public Page<BookDto> searchBooks(BookSearchParametersDto searchParameters,
                                     Pageable pageable) {
        validateSearchParameters(searchParameters);

        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification, pageable)
                .map(bookMapper::toBookDto);
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategoryId(categoryId, pageable)
                .map(bookMapper::toDtoWithoutCategories);
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found with id " + id));
    }

    private void validateSearchParameters(BookSearchParametersDto searchParameters) {
        if (searchParameters == null) {
            throw new IllegalArgumentException("Search parameters cannot be null");
        }
    }

    private void validateIsbn(String isbn) {
        if (bookRepository.existsByIsbn(isbn)) {
            throw new DuplicateIsbnException();
        }
    }

}
