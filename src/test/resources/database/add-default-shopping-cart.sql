INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (1, 'testuser@example.com', 'password123',
        'Test name', 'Test last name', 'Test address', false);

INSERT INTO books (id, title, price, author, isbn, is_deleted)
VALUES (1, 'Test Book Title', 19.99, 'Test Author', '1234567890121', false);

INSERT INTO shopping_carts (id, user_id)
VALUES (1, 1);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (1, 1, 1, 2);
