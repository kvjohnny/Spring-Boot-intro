package mate.academy.book.dto.shoppingcart;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mate.academy.book.dto.cartitem.CartItemResponseDto;

@Getter
@Setter
@ToString
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
