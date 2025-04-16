package com.mate.bookstore.service.order;

import com.mate.bookstore.dto.order.CreateOrderRequestDto;
import com.mate.bookstore.dto.order.OrderDto;
import com.mate.bookstore.dto.order.OrderItemDto;
import com.mate.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.mate.bookstore.exception.EntityNotFoundException;
import com.mate.bookstore.exception.ShoppingCartEmptyException;
import com.mate.bookstore.mapper.OrderMapper;
import com.mate.bookstore.model.CartItem;
import com.mate.bookstore.model.Order;
import com.mate.bookstore.model.OrderItem;
import com.mate.bookstore.model.ShoppingCart;
import com.mate.bookstore.model.User;
import com.mate.bookstore.repository.order.OrderRepository;
import com.mate.bookstore.service.order.item.OrderItemService;
import com.mate.bookstore.service.shoppingcart.ShoppingCartService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final OrderItemService orderItemService;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDto createOrder(CreateOrderRequestDto createOrderRequestDto,
                                User user) {
        ShoppingCart shoppingCart = shoppingCartService.findShoppingCartByUser(user);

        validateShoppingCartNotEmpty(shoppingCart);

        BigDecimal totalPrice = calculateTotal(shoppingCart.getCartItems());

        Order order = buildAndSaveOrder(user, createOrderRequestDto.shippingAddress(), totalPrice);

        Set<OrderItem> orderItems = orderItemService
                .createOrderItemsFromCart(shoppingCart.getCartItems(), order);
        order.setOrderItems(orderItems);
        order = orderRepository.save(order);

        shoppingCartService.clearShoppingCart(shoppingCart);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public List<OrderDto> findAll(User user, Pageable pageable) {
        return orderRepository.findByUser(user, pageable).stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> findOrderItemsByOrderId(Long orderId, User user) {
        Order userOrderById = getUserOrderById(orderId, user);
        return orderItemService.getOrderItemsForOrder(userOrderById);
    }

    @Override
    public OrderItemDto findOrderItemById(Long orderId, Long orderItemId, User user) {
        Order order = getUserOrderById(orderId, user);
        return orderItemService.getOrderItemById(order, orderItemId);

    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long id, UpdateOrderStatusRequestDto requestDto) {
        Order order = findOrderById(id);
        order.setStatus(requestDto.status());
        return orderMapper.toOrderDto(orderRepository.save(order));
    }

    private Order buildAndSaveOrder(User user, String shippingAddress, BigDecimal totalPrice) {
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setTotal(totalPrice);
        return orderRepository.save(order);
    }

    private BigDecimal calculateTotal(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> item.getBook().getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateShoppingCartNotEmpty(ShoppingCart shoppingCart) {
        if (shoppingCart.getCartItems() == null || shoppingCart.getCartItems().isEmpty()) {
            throw new ShoppingCartEmptyException();
        }
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found with id: " + id));
    }

    private Order getUserOrderById(Long id, User user) {
        Order order = findOrderById(id);
        validateOrderOwnership(order, user);
        return order;
    }

    private void validateOrderOwnership(Order order, User user) {
        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Order with id " + order.getId()
                    + " doesn't belong to user " + user.getId());
        }
    }
}
