package com.mate.bookstore.service.order.item;

import com.mate.bookstore.dto.order.OrderItemDto;
import com.mate.bookstore.model.CartItem;
import com.mate.bookstore.model.Order;
import com.mate.bookstore.model.OrderItem;
import java.util.List;
import java.util.Set;

public interface OrderItemService {
    Set<OrderItem> createOrderItemsFromCart(Set<CartItem> cartItems, Order order);

    List<OrderItemDto> getOrderItemsForOrder(Order order);

    OrderItemDto getOrderItemById(Order order, Long orderItemId);
}
