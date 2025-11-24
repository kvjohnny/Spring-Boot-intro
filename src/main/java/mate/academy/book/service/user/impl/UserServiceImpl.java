package mate.academy.book.service.user.impl;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.book.dto.user.UserRegistrationRequestDto;
import mate.academy.book.dto.user.UserResponseDto;
import mate.academy.book.exception.RegistrationException;
import mate.academy.book.mapper.UserMapper;
import mate.academy.book.model.Role;
import mate.academy.book.model.User;
import mate.academy.book.repository.role.RoleRepository;
import mate.academy.book.repository.user.UserRepository;
import mate.academy.book.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto save(UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(registrationRequestDto.getEmail())) {
            throw new RegistrationException("Can't register user by email "
                    + registrationRequestDto.getEmail());
        }
        User user = userMapper.toModel(registrationRequestDto);
        Set<Role> roles = new HashSet<>(roleRepository
                .findAllById(registrationRequestDto.getRoles()));
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(
                registrationRequestDto.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }
}
