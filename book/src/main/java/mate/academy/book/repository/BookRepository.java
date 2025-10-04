package mate.academy.book.repository;

import mate.academy.book.model.Book;

import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
