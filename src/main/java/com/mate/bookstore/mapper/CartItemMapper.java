package com.mate.bookstore.mapper;

import com.mate.bookstore.config.MapperConfig;
import com.mate.bookstore.dto.shoppingcart.CartItemDto;
import com.mate.bookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDto toCartItemDto(CartItem cartItem);
}
