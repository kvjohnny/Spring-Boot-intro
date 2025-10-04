package mate.academy.book.service;

import mate.academy.book.model.Book;

import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
