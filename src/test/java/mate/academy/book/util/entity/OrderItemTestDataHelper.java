package mate.academy.book.util.entity;

import java.math.BigDecimal;
import mate.academy.book.model.Book;
import mate.academy.book.model.Order;
import mate.academy.book.model.OrderItem;

public class OrderItemTestDataHelper {
    public static OrderItem createDefaultOrderItem() {
        Order order = OrderTestDataHelper.createDefaultOrder();
        Long defaultId = 1L;
        Book book = new Book().setId(defaultId).setTitle("Book 1");
        OrderItem orderItem = new OrderItem();
        orderItem.setId(defaultId);
        orderItem.setOrder(order);
        orderItem.setBook(book);
        orderItem.setQuantity(4);
        orderItem.setPrice(BigDecimal.valueOf(25.21));
        return orderItem;
    }
}
