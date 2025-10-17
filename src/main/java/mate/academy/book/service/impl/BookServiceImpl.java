package mate.academy.book.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.book.dto.BookDto;
import mate.academy.book.dto.CreateBookRequestDto;
import mate.academy.book.exception.EntityNotFoundException;
import mate.academy.book.mapper.BookMapper;
import mate.academy.book.model.Book;
import mate.academy.book.repository.BookRepository;
import mate.academy.book.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findBookById(id).orElseThrow(()
                -> new EntityNotFoundException("Can't find book by id " + id));
        return bookMapper.toDto(book);
    }
}
