package mate.academy.book.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import mate.academy.book.dto.orderitem.OrderItemResponseDto;
import mate.academy.book.model.Order;

public record OrderResponseDto(
        Long id,
        Long userId,
        Set<OrderItemResponseDto> orderItems,
        LocalDateTime orderDate,
        BigDecimal total,
        Order.Status status
) {
}
