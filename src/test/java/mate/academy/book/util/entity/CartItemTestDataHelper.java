package mate.academy.book.util.entity;

import mate.academy.book.model.Book;
import mate.academy.book.model.CartItem;

public class CartItemTestDataHelper {
    public static CartItem createDefaultCartItem() {
        return new CartItem().setId(1L);
    }

    public static CartItem createFirstCartItemWithFilledFields() {
        Book book = new Book().setId(1L).setTitle("Book 1");
        return new CartItem()
                .setId(1L)
                .setQuantity(4)
                .setBook(book);
    }

    public static CartItem createSecondCartItemWithFilledFields() {
        Book book = new Book().setId(2L).setTitle("Book 2");
        return new CartItem()
                .setId(2L)
                .setQuantity(5)
                .setBook(book);
    }
}
