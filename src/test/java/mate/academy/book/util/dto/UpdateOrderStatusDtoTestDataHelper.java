package mate.academy.book.util.dto;

import mate.academy.book.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.book.model.Order;

public class UpdateOrderStatusDtoTestDataHelper {
    public static UpdateOrderStatusRequestDto updateOrderStatusRequestDto() {
        return new UpdateOrderStatusRequestDto(Order.Status.COMPLETED);
    }
}
