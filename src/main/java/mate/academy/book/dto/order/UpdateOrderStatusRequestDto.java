package mate.academy.book.dto.order;

import jakarta.validation.constraints.NotNull;
import mate.academy.book.model.Order;

public record UpdateOrderStatusRequestDto(
        @NotNull
        Order.Status status
) {
}
