package mate.academy.book.service.user.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.book.dto.user.UserRegistrationRequestDto;
import mate.academy.book.dto.user.UserResponseDto;
import mate.academy.book.exception.RegistrationException;
import mate.academy.book.mapper.UserMapper;
import mate.academy.book.model.User;
import mate.academy.book.repository.user.UserRepository;
import mate.academy.book.service.user.UserService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto save(UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException {
        if (userRepository.findUserByEmail(registrationRequestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user");
        }
        User user = new User();
        user.setEmail(registrationRequestDto.getEmail());
        user.setPassword(registrationRequestDto.getPassword());
        user.setFirstName(registrationRequestDto.getFirstName());
        user.setLastName(registrationRequestDto.getLastName());
        user.setShippingAddress(registrationRequestDto.getShippingAddress());
        return userMapper.toDto(userRepository.save(user));
    }
}
