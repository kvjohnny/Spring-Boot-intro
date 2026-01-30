package mate.academy.book.util.dto;

import mate.academy.book.dto.cartitem.CartItemRequestDto;
import mate.academy.book.dto.cartitem.CartItemUpdateRequestDto;

public class CartItemDtoTestDataHelper {
    public static CartItemUpdateRequestDto createUpdatedCartItemRequestDto() {
        return new CartItemUpdateRequestDto(10);
    }

    public static CartItemRequestDto createCartItemRequestDto() {
        return new CartItemRequestDto(2L, 5);
    }
}
