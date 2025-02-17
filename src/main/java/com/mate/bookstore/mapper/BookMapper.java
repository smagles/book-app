package com.mate.bookstore.mapper;

import com.mate.bookstore.config.MapperConfig;
import com.mate.bookstore.dto.BookDto;
import com.mate.bookstore.dto.CreateBookRequestDto;
import com.mate.bookstore.dto.UpdateBookRequestDto;
import com.mate.bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toBookDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isbn", ignore = true)
    void updateModel(UpdateBookRequestDto updateBookRequestDto, @MappingTarget Book book);
}
