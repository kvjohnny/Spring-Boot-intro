package mate.academy.book.service;

import java.util.List;
import mate.academy.book.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
