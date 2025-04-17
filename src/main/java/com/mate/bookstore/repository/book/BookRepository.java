package com.mate.bookstore.repository.book;

import com.mate.bookstore.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book> {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book save(Book book);

    void deleteById(Long id);

    boolean existsByIsbn(String isbn);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    Page<Book> findAllByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}
