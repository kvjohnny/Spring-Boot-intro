package mate.academy.book.service;

import java.util.List;
import mate.academy.book.dto.BookDto;
import mate.academy.book.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    void deleteBookById(Long id);

    BookDto updateBookById(Long id, CreateBookRequestDto requestDto);
}
