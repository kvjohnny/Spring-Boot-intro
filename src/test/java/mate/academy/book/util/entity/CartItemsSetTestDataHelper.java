package mate.academy.book.util.entity;

import java.util.HashSet;
import java.util.Set;
import mate.academy.book.model.CartItem;

public class CartItemsSetTestDataHelper {
    public static Set<CartItem> cartItemsWithSingleItem(CartItem cartItem) {
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        return cartItems;
    }
}
