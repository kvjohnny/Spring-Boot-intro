package mate.academy.book.util.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import mate.academy.book.dto.order.OrderRequestDto;
import mate.academy.book.dto.order.OrderResponseDto;
import mate.academy.book.dto.orderitem.OrderItemResponseDto;
import mate.academy.book.model.Order;

public class OrderDtoTestDataHelper {
    public static OrderRequestDto createOrderRequestDto() {
        return new OrderRequestDto("London");
    }

    public static OrderResponseDto createOrderResponseDto() {
        return new OrderResponseDto(
                1L,
                1L,
                Set.of(new OrderItemResponseDto(1L, 1L, 4)),
                LocalDateTime.of(2025, 12, 15, 19, 10, 44),
                BigDecimal.valueOf(100.84),
                Order.Status.PENDING
        );
    }

    public static OrderResponseDto updateOrderResponseDto() {
        return new OrderResponseDto(
                1L,
                1L,
                Set.of(new OrderItemResponseDto(1L, 1L, 4)),
                LocalDateTime.of(2025, 12, 15, 19, 10, 44),
                BigDecimal.valueOf(100.84),
                Order.Status.COMPLETED
        );
    }
}
