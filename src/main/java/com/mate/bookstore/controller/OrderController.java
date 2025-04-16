package com.mate.bookstore.controller;

import com.mate.bookstore.controller.openapi.OrderApi;
import com.mate.bookstore.dto.order.CreateOrderRequestDto;
import com.mate.bookstore.dto.order.OrderDto;
import com.mate.bookstore.dto.order.OrderItemDto;
import com.mate.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.mate.bookstore.model.User;
import com.mate.bookstore.service.order.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController implements OrderApi {
    private final OrderService orderService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> getOrders(@AuthenticationPrincipal User user,
                                    @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return orderService.findAll(user, pageable);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@RequestBody CreateOrderRequestDto requestDto,
                                @AuthenticationPrincipal User user) {
        return orderService.createOrder(requestDto, user);
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDto updateOrderStatus(@PathVariable Long id,
                                      @RequestBody UpdateOrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }

    @GetMapping("{orderId}/items")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderId,
                                            @AuthenticationPrincipal User user) {
        return orderService.findOrderItemsByOrderId(orderId, user);
    }

    @GetMapping("{orderId}/items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderItemDto getOrderItem(@PathVariable Long orderId,
                                     @PathVariable Long id,
                                     @AuthenticationPrincipal User user) {
        return orderService.findOrderItemById(orderId, id, user);
    }
}
