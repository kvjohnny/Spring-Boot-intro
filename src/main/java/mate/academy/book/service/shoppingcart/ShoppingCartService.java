package mate.academy.book.service.shoppingcart;

import mate.academy.book.dto.cartitem.CartItemRequestDto;
import mate.academy.book.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.book.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart(String email);

    ShoppingCartResponseDto updateQuantityOfBookInShoppingCart(
            Long id, String email, CartItemUpdateRequestDto requestDto);

    void deleteCartItem(String email, Long cartItemId);

    ShoppingCartResponseDto addCartItemToShoppingCart(
            String email, CartItemRequestDto requestDto);

    void registerShoppingCard(String email);
}
