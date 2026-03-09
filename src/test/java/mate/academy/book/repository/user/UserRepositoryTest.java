package mate.academy.book.repository.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import mate.academy.book.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {
        "classpath:database/roles/add-one-role-to-roles-table.sql",
        "classpath:database/users/add-three-users-to-users-table.sql",
        "classpath:database/usersroles/add-three-users-roles-to-users-roles-table.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/usersroles/remove-users-roles-from-users-roles-table.sql",
        "classpath:database/users/remove-users-from-users-table.sql",
        "classpath:database/roles/remove-roles-from-roles-table.sql"
},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Find user by email")
    void findUserByEmail_WhenUserExists_ReturnsUser() {
        String email = "example@gmail.com";
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        assertThat(optionalUser).isPresent();
        User user = optionalUser.get();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("Find user by invalid email")
    void findUserByEmail_WhenUserDoesNotExist_ReturnsEmpty() {
        String email = "invalid_email@gmail.com";
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        assertThat(optionalUser).isEmpty();
    }
}
