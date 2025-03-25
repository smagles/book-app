package com.mate.bookstore.repository.book.spec;

import com.mate.bookstore.model.Book;
import com.mate.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    public Specification<Book> getSpecification(String [] params) {
        return (root, query, criteriaBuilder) -> root.get("title")
                .in(params);
    }
}
