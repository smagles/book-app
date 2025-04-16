package com.mate.bookstore.service.order;

import com.mate.bookstore.dto.order.CreateOrderRequestDto;
import com.mate.bookstore.dto.order.OrderDto;
import com.mate.bookstore.dto.order.OrderItemDto;
import com.mate.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.mate.bookstore.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequestDto createOrderRequestDto, User user);

    List<OrderDto> findAll(User user, Pageable pageable);

    OrderDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto updateOrderStatusRequestDto);

    List<OrderItemDto> findOrderItemsByOrderId(Long orderId, User user);

    OrderItemDto findOrderItemById(Long orderId, Long orderItemId, User user);

}
