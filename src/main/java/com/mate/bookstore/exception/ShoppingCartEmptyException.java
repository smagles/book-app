package com.mate.bookstore.exception;

public class ShoppingCartEmptyException extends RuntimeException {
    public ShoppingCartEmptyException() {
        super("Shopping cart is empty. Cannot proceed with order checkout");
    }
}
