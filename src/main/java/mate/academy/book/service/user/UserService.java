package mate.academy.book.service.user;

import mate.academy.book.dto.user.UserRegistrationRequestDto;
import mate.academy.book.dto.user.UserResponseDto;
import mate.academy.book.exception.RegistrationException;

public interface UserService {
    UserResponseDto save(
            UserRegistrationRequestDto registrationRequestDto) throws RegistrationException;
}
