package mate.academy.book.dto.order;

import jakarta.validation.constraints.NotBlank;

public record OrderRequestDto(
        @NotBlank
        String shippingAddress
) {
}
