package com.mate.bookstore.service.order.item;

import com.mate.bookstore.dto.order.OrderItemDto;
import com.mate.bookstore.exception.EntityNotFoundException;
import com.mate.bookstore.mapper.OrderItemMapper;
import com.mate.bookstore.model.Book;
import com.mate.bookstore.model.CartItem;
import com.mate.bookstore.model.Order;
import com.mate.bookstore.model.OrderItem;
import com.mate.bookstore.repository.order.OrderItemRepository;
import com.mate.bookstore.service.book.BookService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final BookService bookService;

    @Override
    public Set<OrderItem> createOrderItemsFromCart(Set<CartItem> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem -> convertCartItemToOrderItem(cartItem, order))
                .collect(Collectors.toSet());
    }

    @Override
    public List<OrderItemDto> getOrderItemsForOrder(Order order) {
        return order.getOrderItems().stream()
                .map(orderItemMapper::toOrderItemDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemById(Order order, Long orderItemId) {
        return orderItemMapper.toOrderItemDto(findOrderItemFromOrder(order, orderItemId));

    }

    private OrderItem convertCartItemToOrderItem(CartItem cartItem, Order order) {
        validateBookExists(cartItem);
        BigDecimal total = calculateItemTotal(cartItem.getBook(),
                cartItem.getQuantity());
        return orderItemMapper.toOrderItem(cartItem, order, total);
    }

    private BigDecimal calculateItemTotal(Book book, int quantity) {
        if (book == null || book.getPrice() == null) {
            throw new IllegalArgumentException("Book or price cannot be null");
        }
        return book.getPrice().multiply(new BigDecimal(quantity));
    }

    private OrderItem findOrderItemFromOrder(Order order, Long orderItemId) {
        return order.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(()
                        -> new EntityNotFoundException(
                        "Order item does not belong to the order"));
    }

    private void validateBookExists(CartItem cartItem) {
        if (cartItem.getBook() == null || cartItem.getBook().getId() == null) {
            throw new IllegalArgumentException(
                    "Book in cart item must not be null and must have an ID");
        }
        bookService.getBookById(cartItem.getBook().getId());
    }
}
