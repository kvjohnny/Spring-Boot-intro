package mate.academy.book.repository;

import java.util.List;
import java.util.Optional;
import mate.academy.book.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findBookById(Long id);
}
