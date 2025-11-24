package mate.academy.book.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.book.config.MapperConfig;
import mate.academy.book.dto.user.UserRegistrationRequestDto;
import mate.academy.book.dto.user.UserResponseDto;
import mate.academy.book.model.Role;
import mate.academy.book.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "roles", source = "roles")
    User toModel(UserRegistrationRequestDto registrationRequestDto);

    default Set<Role> mapRoles(Set<Long> roleIds) {
        if (roleIds == null) {
            return new HashSet<>();
        }
        return roleIds.stream()
                .map(id -> {
                    Role role = new Role();
                    role.setId(id);
                    return role;
                })
                .collect(Collectors.toSet());
    }

    UserResponseDto toDto(User user);
}
