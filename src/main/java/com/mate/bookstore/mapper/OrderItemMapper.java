package com.mate.bookstore.mapper;

import com.mate.bookstore.config.MapperConfig;
import com.mate.bookstore.dto.order.OrderItemDto;
import com.mate.bookstore.model.CartItem;
import com.mate.bookstore.model.Order;
import com.mate.bookstore.model.OrderItem;
import java.math.BigDecimal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    OrderItem toOrderItem(CartItem cartItem, Order order, BigDecimal price);

    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toOrderItemDto(OrderItem orderItem);
}
