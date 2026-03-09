package mate.academy.book.util.entity;

import java.util.Set;
import mate.academy.book.model.Role;
import mate.academy.book.model.User;

public class UserTestDataHelper {
    public static User createDefaultUser() {
        User user = new User();
        Long userId = 1L;
        String email = "example@gmail.com";
        user.setId(userId);
        user.setEmail(email);
        return user;
    }

    public static User createDefaultUserWithRole() {
        User user = createDefaultUser();
        user.setPassword("password");
        user.setFirstName("Bob");
        user.setLastName("Johnson");
        user.setShippingAddress("London");
        Role role = new Role();
        role.setName(Role.RoleName.USER);
        user.setRoles(Set.of(role));
        return user;
    }
}
