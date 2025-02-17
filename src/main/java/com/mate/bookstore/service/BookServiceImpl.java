package com.mate.bookstore.service;

import com.mate.bookstore.dto.BookDto;
import com.mate.bookstore.dto.CreateBookRequestDto;
import com.mate.bookstore.dto.UpdateBookRequestDto;
import com.mate.bookstore.exception.EntityNotFoundException;
import com.mate.bookstore.mapper.BookMapper;
import com.mate.bookstore.model.Book;
import com.mate.bookstore.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto createBookRequestDto) {
        Book book = bookMapper.toModel(createBookRequestDto);
        book = bookRepository.save(book);
        return bookMapper.toBookDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toBookDto(getBookById(id));
    }

    @Override
    public void deleteById(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    @Override
    public BookDto update(Long id, UpdateBookRequestDto updateRequestDto) {
        Book existingBook = getBookById(id);
        bookMapper.updateModel(updateRequestDto, existingBook);
        existingBook = bookRepository.save(existingBook);
        return bookMapper.toBookDto(existingBook);
    }

    private Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found with id " + id));
    }
}

