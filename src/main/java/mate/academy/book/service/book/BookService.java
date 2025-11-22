package mate.academy.book.service.book;

import java.util.List;
import mate.academy.book.dto.book.BookDto;
import mate.academy.book.dto.book.BookSearchParametersDto;
import mate.academy.book.dto.book.CreateBookRequestDto;
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
