package mate.academy.book.util.dto;

import java.util.Set;
import mate.academy.book.dto.cartitem.CartItemResponseDto;
import mate.academy.book.dto.shoppingcart.ShoppingCartResponseDto;

public class ShoppingCartDtoTestDataHelper {
    public static ShoppingCartResponseDto createShoppingCartResponseDto() {
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        CartItemResponseDto cartItemResponseDto =
                new CartItemResponseDto(1L, 1L, "Book 1", 4);
        shoppingCartResponseDto.setId(1L);
        shoppingCartResponseDto.setUserId(1L);
        shoppingCartResponseDto.setCartItems(Set.of(cartItemResponseDto));
        return shoppingCartResponseDto;
    }

    public static ShoppingCartResponseDto createShoppingCartResponseDtoWithUpdatedBooksQuantity() {
        ShoppingCartResponseDto cartResponseDto = new ShoppingCartResponseDto();
        CartItemResponseDto cartItemResponseDto =
                new CartItemResponseDto(1L, 1L, "Book 1", 10);
        cartResponseDto.setId(1L);
        cartResponseDto.setUserId(1L);
        cartResponseDto.setCartItems(Set.of(cartItemResponseDto));
        return cartResponseDto;
    }

    public static ShoppingCartResponseDto createUpdatedShoppingCartResponseDtoWithTwoCartItems() {
        ShoppingCartResponseDto cartResponseDto = new ShoppingCartResponseDto();
        CartItemResponseDto cartItemResponseDto1 =
                new CartItemResponseDto(1L, 1L, "Book 1", 4);
        cartResponseDto.setId(1L);
        cartResponseDto.setUserId(1L);
        CartItemResponseDto cartItemResponseDto2 =
                new CartItemResponseDto(2L, 2L, "Book 2", 5);
        cartResponseDto.setCartItems(Set.of(cartItemResponseDto1, cartItemResponseDto2));
        return cartResponseDto;
    }
}
