package mate.academy.book.util.dto;

import mate.academy.book.dto.orderitem.OrderItemResponseDto;

public class OrderItemDtoTestDataHelper {
    public static OrderItemResponseDto createDefaultOrderItemDto() {
        return new OrderItemResponseDto(1L, 1L, 4);
    }
}
