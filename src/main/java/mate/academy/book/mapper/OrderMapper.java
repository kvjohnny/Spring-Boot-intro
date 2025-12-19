package mate.academy.book.mapper;

import mate.academy.book.config.MapperConfig;
import mate.academy.book.dto.order.OrderRequestDto;
import mate.academy.book.dto.order.OrderResponseDto;
import mate.academy.book.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderResponseDto toDto(Order order);

    Order toModel(OrderRequestDto requestDto);
}
