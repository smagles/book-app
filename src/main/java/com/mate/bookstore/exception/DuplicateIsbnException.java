package com.mate.bookstore.exception;

public class DuplicateIsbnException extends RuntimeException {
    public DuplicateIsbnException() {
        super("ISBN must be unique");
    }
}
