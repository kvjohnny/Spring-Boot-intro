package mate.academy.book.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import mate.academy.book.validation.Email;
import mate.academy.book.validation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@EqualsAndHashCode
@FieldMatch(
        fields = {"password", "repeatPassword"},
        message = "Password and repeat password must be equal"
)
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 8, max = 20)
    private String password;
    @NotBlank
    @Length(min = 8, max = 20)
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
