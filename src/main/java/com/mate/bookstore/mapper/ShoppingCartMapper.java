package com.mate.bookstore.mapper;

import com.mate.bookstore.config.MapperConfig;
import com.mate.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.mate.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

}
