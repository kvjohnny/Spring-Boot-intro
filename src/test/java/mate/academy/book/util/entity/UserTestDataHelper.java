package mate.academy.book.util.entity;

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
}
