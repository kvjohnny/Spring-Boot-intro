package mate.academy.book.repository.order;

import java.util.List;
import java.util.Optional;
import mate.academy.book.model.Order;
import mate.academy.book.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> getOrderById(Long id);

    @Query("SELECT o FROM Order o "
            + "LEFT JOIN o.orderItems "
            + "WHERE o.user.id =:userId")
    Page<Order> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT o.orderItems FROM Order o "
            + "LEFT JOIN o.orderItems "
            + "WHERE o.id = :orderId "
            + "AND o.user.id =:userId")
    List<OrderItem> findAllOrderItemsByOrderIdAndUserId(
            @Param("orderId") Long orderId,
            @Param("userId") Long userId);
}
