package mate.academy.book.repository.user;

import java.util.Optional;
import mate.academy.book.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findUserByEmailWithRoles(@Param("email") String email);

    boolean existsByEmail(String email);
}
