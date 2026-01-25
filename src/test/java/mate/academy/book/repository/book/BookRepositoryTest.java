package mate.academy.book.repository.book;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import mate.academy.book.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({
        BookSpecificationProvider.class,
})
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookSpecificationProvider bookSpecificationProvider;

    @Test
    @DisplayName("Find all specific books")
    @Sql(scripts = "classpath:database/books/add-three-books-to-books-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-books-from-books-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_GivenSpecificBooks_ReturnsAllSpecificBooks() {
        Specification<Book> spec =
                bookSpecificationProvider.getSpecification("title", "book");
        Specification<Book> specification =
                bookSpecificationProvider.getSpecification("author", "author").and(spec);
        List<Book> actual = bookRepository.findAll(specification);
        assertThat(actual).hasSize(3);
    }

    @Test
    @DisplayName("Find all books by category id")
    @Sql(scripts = {
            "classpath:database/books/add-three-books-to-books-table.sql",
            "classpath:database/categories/add-two-categories-table.sql",
            "classpath:database/bookscategories/add-three-books-categories-to-books-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/bookscategories/remove-books-categories-from-books-categories-table.sql",
            "classpath:database/categories/remove-categories-from-categories-table.sql",
            "classpath:database/books/remove-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_GivenBooksByCategoryId_ReturnsAllBooksByCategoryId() {
        List<Book> actual = bookRepository.findAllByCategoryId(1L);
        List<String> actual2 = actual.stream().map(Book::getTitle).toList();
        List<String> expected2 = List.of("Book 1", "Book 2");
        assertThat(actual).hasSize(2);
        assertThat(actual2)
                .usingRecursiveComparison()
                .isEqualTo(expected2);
    }
}
