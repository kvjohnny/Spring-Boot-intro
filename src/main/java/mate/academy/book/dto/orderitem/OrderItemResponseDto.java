package mate.academy.book.dto.orderitem;

public record OrderItemResponseDto(
        Long id,
        Long bookId,
        int quantity
) {
}
