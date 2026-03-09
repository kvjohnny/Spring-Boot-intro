package mate.academy.book.util.dto;

import mate.academy.book.dto.user.UserLoginRequestDto;
import mate.academy.book.dto.user.UserRegistrationRequestDto;
import mate.academy.book.dto.user.UserResponseDto;

public class UserDtoTestDataHelper {
    public static UserRegistrationRequestDto createUserRequestDto() {
        UserRegistrationRequestDto userRequestDto = new UserRegistrationRequestDto();
        userRequestDto.setEmail("example@gmail.com");
        userRequestDto.setPassword("password");
        userRequestDto.setRepeatPassword("password");
        userRequestDto.setFirstName("Bob");
        userRequestDto.setLastName("Johnson");
        userRequestDto.setShippingAddress("London");
        return userRequestDto;
    }

    public static UserResponseDto createUserResponseDto() {
        return new UserResponseDto(
                1L,
                "example@gmail.com",
                "Bob",
                "Johnson",
                "London"
        );
    }

    public static UserLoginRequestDto createUserLoginRequestDto() {
        return new UserLoginRequestDto(
                "example@gmail.com",
                "password"
        );
    }
}
