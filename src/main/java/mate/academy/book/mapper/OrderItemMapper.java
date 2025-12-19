package mate.academy.book.mapper;

import mate.academy.book.config.MapperConfig;
import mate.academy.book.dto.orderitem.OrderItemResponseDto;
import mate.academy.book.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toDto(OrderItem orderItem);
}
