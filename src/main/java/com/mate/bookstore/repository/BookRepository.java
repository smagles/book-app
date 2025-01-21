package com.mate.bookstore.repository;

import com.mate.bookstore.model.Book;

import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

}
