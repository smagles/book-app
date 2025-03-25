package com.mate.bookstore.repository.book.spec;

import com.mate.bookstore.model.Book;
import com.mate.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "author";
    }

    public Specification<Book> getSpecification(String [] params) {
        return (root, query, criteriaBuilder) -> root.get("author")
                .in(params);
    }
}
