package mate.academy.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.book.dto.user.UserLoginRequestDto;
import mate.academy.book.dto.user.UserLoginResponseDto;
import mate.academy.book.dto.user.UserRegistrationRequestDto;
import mate.academy.book.dto.user.UserResponseDto;
import mate.academy.book.exception.RegistrationException;
import mate.academy.book.security.AuthenticationService;
import mate.academy.book.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
@Tag(name = "User management", description = "Endpoints for managing users")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user", description = "Register a new user")
    public UserResponseDto save(
            @RequestBody @Valid UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException {
        return userService.save(registrationRequestDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticate user")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }

}
