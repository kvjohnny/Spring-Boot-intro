package mate.academy.book.mapper;

import mate.academy.book.config.MapperConfig;
import mate.academy.book.model.CartItem;
import mate.academy.book.model.Order;
import mate.academy.book.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(
            target = "price",
            expression = "java(cartItem.getBook().getPrice())"
    )
    OrderItem toOrderItem(CartItem cartItem, @Context Order order);

    @AfterMapping
    default void setOrder(
            @MappingTarget OrderItem orderItem,
            @Context Order order
    ) {
        orderItem.setOrder(order);
    }
}
