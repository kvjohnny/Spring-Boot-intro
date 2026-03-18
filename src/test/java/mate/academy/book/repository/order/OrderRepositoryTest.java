package mate.academy.book.repository.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import mate.academy.book.model.Order;
import mate.academy.book.model.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {
        "classpath:database/users/add-three-users-to-users-table.sql",
        "classpath:database/books/add-one-book-to-books-table.sql",
        "classpath:database/orders/add-one-order-to-orders-table.sql",
        "classpath:database/orderitems/add-one-order-item-to-order-items-table.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/orderitems/remove-order-item-from-order-items-table.sql",
        "classpath:database/orders/remove-orders-from-orders-table.sql",
        "classpath:database/books/remove-books-from-books-table.sql",
        "classpath:database/users/remove-users-from-users-table.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Find all orders by user id")
    void findAllByUserId_WhenUserExists_ReturnsUserOrders() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<Order> orderPage = orderRepository.findAllByUserId(1L, pageRequest);
        List<Order> actual = orderPage.toList();
        List<String> expected2 = List.of("London");
        List<String> actual2 = actual.stream().map(Order::getShippingAddress).toList();
        assertThat(actual).hasSize(1);
        Order order = actual.get(0);
        assertThat(order.getUser().getId()).isEqualTo(1L);
        assertThat(actual2).isEqualTo(expected2);
        assertThat(orderPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Find all orders by order id and user id")
    void findAllByOrderIdAndUserId_WhenOrderBelongsToUser_ReturnsOrderItems() {
        List<OrderItem> actual =
                orderRepository.findAllOrderItemsByOrderIdAndUserId(1L, 1L);
        assertThat(actual).hasSize(1);
        OrderItem orderItem = actual.get(0);
        assertThat(orderItem.getOrder().getId()).isEqualTo(1L);
        assertThat(orderItem.getBook().getId()).isEqualTo(1L);
        assertThat(orderItem.getOrder().getUser().getId()).isEqualTo(1L);
        assertThat(orderItem.getQuantity()).isEqualTo(4);
        assertThat(orderItem.getPrice()).isEqualByComparingTo("25.21");
    }

    @Test
    @DisplayName("Find orders when order does not belong to user")
    void findAllByOrderIdAndUserId_WhenOrderDoesNotBelongToUser_ReturnsEmptyList() {
        List<OrderItem> actual =
                orderRepository.findAllOrderItemsByOrderIdAndUserId(1L, 2L);
        assertThat(actual).isEmpty();
    }
}
