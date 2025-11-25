package mate.academy.book.repository.role;

import java.util.Set;
import mate.academy.book.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Set<Role> findByName(Role.RoleName name);
}
