package mate.academy.book.repository.book;

import java.util.List;
import java.util.Optional;
import mate.academy.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @NonNull
    @EntityGraph(attributePaths = "categories")
    Page<Book> findAll(@NonNull Pageable pageable);

    @NonNull
    @EntityGraph(attributePaths = "categories")
    List<Book> findAll(@NonNull Specification<Book> specification);

    @EntityGraph(attributePaths = "categories")
    Optional<Book> getBookById(Long id);

    @EntityGraph(attributePaths = "categories")
    @Query("SELECT DISTINCT b FROM Book b "
            + "LEFT JOIN b.categories с WHERE с.id = :categoryId")
    List<Book> findAllByCategoryId(@Param("categoryId") Long categoryId);
}
