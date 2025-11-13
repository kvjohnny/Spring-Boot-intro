package mate.academy.book.service;

import java.util.List;
import mate.academy.book.dto.BookDto;
import mate.academy.book.dto.BookSearchParametersDto;
import mate.academy.book.dto.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    void deleteBookById(Long id);

    BookDto updateBookById(Long id, CreateBookRequestDto requestDto);

    List<BookDto> searchBooks(BookSearchParametersDto searchParametersDto);
}
