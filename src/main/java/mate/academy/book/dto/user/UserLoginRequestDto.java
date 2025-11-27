package mate.academy.book.dto.user;

import jakarta.validation.constraints.NotBlank;
import mate.academy.book.validation.Email;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Length(min = 8, max = 20)
        String password
) {
}
