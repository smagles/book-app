package com.mate.bookstore;

import com.mate.bookstore.model.Book;
import com.mate.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book prideAndPrejudice = new Book();
            prideAndPrejudice.setTitle("Pride and Prejudice");
            prideAndPrejudice.setAuthor("Jane Austen");
            prideAndPrejudice.setPrice(BigDecimal.valueOf(56.00));
            prideAndPrejudice.setIsbn("978-3-16-148410-0");
            System.out.println(bookService.save(prideAndPrejudice));
        };

    }
}
