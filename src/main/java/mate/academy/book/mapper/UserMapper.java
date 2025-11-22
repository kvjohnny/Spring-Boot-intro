package mate.academy.book.mapper;

import mate.academy.book.config.MapperConfig;
import mate.academy.book.dto.user.UserRegistrationRequestDto;
import mate.academy.book.dto.user.UserResponseDto;
import mate.academy.book.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto registrationRequestDto);

    UserResponseDto toDto(User user);
}
