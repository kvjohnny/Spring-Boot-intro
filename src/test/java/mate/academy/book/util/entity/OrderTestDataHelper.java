package mate.academy.book.util.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import mate.academy.book.model.Order;
import mate.academy.book.model.User;

public class OrderTestDataHelper {
    public static Order createDefaultOrder() {
        User user = UserTestDataHelper.createDefaultUserWithRole();
        Order order = new Order();
        Long orderId = 1L;
        order.setId(orderId);
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setTotal(BigDecimal.valueOf(100.84));
        order.setOrderDate(LocalDateTime.of(2025, 12, 15, 19, 10, 44));
        order.setShippingAddress("London");
        return order;
    }
}
