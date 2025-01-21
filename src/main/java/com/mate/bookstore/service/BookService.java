package com.mate.bookstore.service;

import com.mate.bookstore.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();

}
