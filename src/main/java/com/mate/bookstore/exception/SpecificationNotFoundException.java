package com.mate.bookstore.exception;

public class SpecificationNotFoundException extends RuntimeException {
    public SpecificationNotFoundException(String key) {
        super("No SpecificationProvider found for key: " + key);
    }
}
