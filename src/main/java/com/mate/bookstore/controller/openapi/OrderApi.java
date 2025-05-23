package com.mate.bookstore.controller.openapi;

import com.mate.bookstore.dto.order.CreateOrderRequestDto;
import com.mate.bookstore.dto.order.OrderDto;
import com.mate.bookstore.dto.order.OrderItemDto;
import com.mate.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.mate.bookstore.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Order API", description = "Operations related to user orders")
@RequestMapping("/api/orders")
public interface OrderApi {

    @Operation(summary = "Get all orders for the authenticated user")
    @ApiResponse(responseCode = "200", description = "List of orders returned successfully")
    @GetMapping
    Page<OrderDto> getOrders(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                             @Parameter(description = "Pagination info") Pageable pageable);

    @Operation(summary = "Create a new order from the shopping cart")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Shopping cart is empty"),
            @ApiResponse(responseCode = "404", description = "Shopping cart not found")
    })
    @PostMapping
    @RequestBody(
            description = "Order creation data",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CreateOrderRequestDto.class),
                    examples = @ExampleObject(
                            name = "CreateOrderRequest",
                            summary = "Typical order creation request",
                            value = """
                                    {
                                      "shippingAddress": "123 Main St, City, Country"
                                      }
                                    """

                    )
            )
    )
    OrderDto createOrder(
            @RequestBody CreateOrderRequestDto requestDto,
            @Parameter(hidden = true) @AuthenticationPrincipal User user);

    @Operation(summary = "Update the status of an order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("{id}")
    OrderDto updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusRequestDto requestDto);

    @Operation(summary = "Get all items for a specific order")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "List of order items returned successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied to the order"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping("{orderId}/items")
    List<OrderItemDto> getOrderItems(
            @PathVariable Long orderId,
            @Parameter(hidden = true) @AuthenticationPrincipal User user);

    @Operation(summary = "Get a specific order item from an order")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order item returned successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied to the order"),
            @ApiResponse(responseCode = "404", description = "Order or Order Item not found")
    })
    @GetMapping("{orderId}/items/{id}")
    OrderItemDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal User user);
}
