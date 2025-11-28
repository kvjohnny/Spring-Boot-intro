package mate.academy.book.repository.category;

import mate.academy.book.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    void deleteCategoryById(Long id);
}
