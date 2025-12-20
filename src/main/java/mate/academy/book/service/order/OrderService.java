package mate.academy.book.service.order;

import java.util.List;
import mate.academy.book.dto.order.OrderRequestDto;
import mate.academy.book.dto.order.OrderResponseDto;
import mate.academy.book.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.book.dto.orderitem.OrderItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto completeOrder(String email, OrderRequestDto requestDto);

    Page<OrderResponseDto> getOrderHistory(String email, Pageable pageable);

    List<OrderItemResponseDto> getAllOrderItemsByOrderId(String email, Long orderId);

    OrderItemResponseDto getOrderItemByIdAndOrderId(String email, Long orderItemId, Long orderId);

    OrderResponseDto updateOrderStatus(
            Long orderId,
            UpdateOrderStatusRequestDto requestDto);
}
