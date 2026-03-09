package mate.academy.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import mate.academy.book.dto.user.UserRegistrationRequestDto;
import mate.academy.book.dto.user.UserResponseDto;
import mate.academy.book.exception.RegistrationException;
import mate.academy.book.mapper.UserMapper;
import mate.academy.book.model.Role;
import mate.academy.book.model.User;
import mate.academy.book.repository.role.RoleRepository;
import mate.academy.book.repository.user.UserRepository;
import mate.academy.book.service.shoppingcart.ShoppingCartService;
import mate.academy.book.service.user.impl.UserServiceImpl;
import mate.academy.book.util.dto.UserDtoTestDataHelper;
import mate.academy.book.util.entity.UserTestDataHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("Save a new user")
    void saveUser_WithValidData_ReturnsUserDto() throws RegistrationException {
        UserRegistrationRequestDto userRequestDto =
                UserDtoTestDataHelper.createUserRequestDto();
        User user = UserTestDataHelper.createDefaultUserWithRole();
        UserResponseDto expected = UserDtoTestDataHelper.createUserResponseDto();
        when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(false);
        when(userMapper.toModel(userRequestDto)).thenReturn(user);
        when(passwordEncoder.encode(userRequestDto.getPassword())).thenReturn(user.getPassword());
        when(roleRepository.findByName(Role.RoleName.USER)).thenReturn(user.getRoles());
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(expected);
        UserResponseDto actual = userServiceImpl.save(userRequestDto);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        verify(userRepository, times(1)).existsByEmail(userRequestDto.getEmail());
        verify(userMapper, times(1)).toModel(userRequestDto);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
        verify(passwordEncoder, times(1)).encode(userRequestDto.getPassword());
        verify(roleRepository, times(1))
                .findByName(Role.RoleName.USER);
        verify(shoppingCartService, times(1)).registerShoppingCard(user.getEmail());
        verifyNoMoreInteractions(
                userRepository,
                userMapper,
                passwordEncoder,
                roleRepository,
                shoppingCartService
        );
    }

    @Test
    @DisplayName("Save user with existing email throws RegistrationException")
    void saveUser_WhenEmailExists_ThrowsRegistrationException() {
        UserRegistrationRequestDto userRequestDto =
                UserDtoTestDataHelper.createUserRequestDto();
        when(userRepository.existsByEmail(userRequestDto.getEmail()))
                .thenReturn(true);
        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> userServiceImpl.save(userRequestDto));
        String expected = "Can't register user by email " + userRequestDto.getEmail();
        String actual = exception.getMessage();
        assertThat(actual)
                .isEqualTo(expected);
        verify(userRepository).existsByEmail(userRequestDto.getEmail());
        verifyNoMoreInteractions(userRepository);
    }
}
