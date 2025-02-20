package com.mate.bookstore.repository.book;

import com.mate.bookstore.dto.BookSearchParametersDto;
import com.mate.bookstore.model.Book;
import com.mate.bookstore.repository.SpecificationBuilder;
import com.mate.bookstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> spec = Specification.where(null);
        if (bookSearchParametersDto.titles() != null
                && bookSearchParametersDto.titles().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("title")
                    .getSpecification(bookSearchParametersDto.titles()));
        }
        if (bookSearchParametersDto.authors() != null
                && bookSearchParametersDto.authors().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(bookSearchParametersDto.authors()));
        }
        return spec;
    }
}
