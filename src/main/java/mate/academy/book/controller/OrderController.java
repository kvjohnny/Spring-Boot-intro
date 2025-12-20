package mate.academy.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.book.dto.order.OrderRequestDto;
import mate.academy.book.dto.order.OrderResponseDto;
import mate.academy.book.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.book.dto.orderitem.OrderItemResponseDto;
import mate.academy.book.model.User;
import mate.academy.book.service.order.OrderService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/orders")
@RequiredArgsConstructor
@RestController
@Tag(name = "Order management", description = "Endpoints for managing orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto completeOrder(
            Authentication authentication,
            @RequestBody @Valid OrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.completeOrder(user.getEmail(), requestDto);
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Get a list of all orders")
    public Page<OrderResponseDto> getOrderHistory(
            Authentication authentication,
            @ParameterObject Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderHistory(user.getEmail(), pageable);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all order items by order id",
            description = "Get all order items by order id")
    public List<OrderItemResponseDto> getAllOrderItemsByOrderId(
            Authentication authentication,
            @PathVariable Long orderId) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllOrderItemsByOrderId(user.getEmail(), orderId);
    }

    @GetMapping("/{orderId}/items/{id}")
    @Operation(summary = "Get the order item by id and order id",
            description = "Get the order item by id and order id")
    public OrderItemResponseDto getOrderItemByIdAndOrderId(
            Authentication authentication,
            @PathVariable("id") Long orderItemId,
            @PathVariable("orderId") Long orderId
    ) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemByIdAndOrderId(user.getEmail(), orderItemId, orderId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{id}")
    @Operation(summary = "Update the order's status",
            description = "Update the order's status")
    public OrderResponseDto updateOrderStatus(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateOrderStatusRequestDto requestDto
    ) {
        return orderService.updateOrderStatus(id, requestDto);
    }
}
