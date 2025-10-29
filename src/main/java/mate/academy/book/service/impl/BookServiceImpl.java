package mate.academy.book.service.impl;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import mate.academy.book.dto.BookDto;
import mate.academy.book.dto.BookSearchParametersDto;
import mate.academy.book.dto.CreateBookRequestDto;
import mate.academy.book.exception.EntityNotFoundException;
import mate.academy.book.mapper.BookMapper;
import mate.academy.book.model.Book;
import mate.academy.book.repository.book.BookRepository;
import mate.academy.book.repository.book.BookSearchParametersMapper;
import mate.academy.book.repository.book.SpecificationProvider;
import mate.academy.book.service.BookService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final SpecificationProvider<Book> bookSpecificationProvider;
    private final BookSearchParametersMapper bookSearchParametersMapper;

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
        Book book = bookRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Can't find book by id " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBookById(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                "Can't find book by id " + id));
        bookMapper.toUpdatedModel(book, requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> search(Map<String, String> param) {
        Specification<Book> specification = Specification.not(null);
        for (Map.Entry<String, String> entry : param.entrySet()) {
            Specification<Book> sp = bookSpecificationProvider
                    .getSpecification(entry.getKey(), entry.getValue());
            specification = specification.and(sp);
        }
        return bookRepository.findAll(specification).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto searchParameters) {
        Map<String, String> param = bookSearchParametersMapper.toMap(searchParameters);
        return search(param);
    }

}
