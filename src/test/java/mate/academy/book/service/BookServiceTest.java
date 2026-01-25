package mate.academy.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import mate.academy.book.dto.book.BookDto;
import mate.academy.book.dto.book.BookSearchParametersDto;
import mate.academy.book.dto.book.CreateBookRequestDto;
import mate.academy.book.exception.EntityNotFoundException;
import mate.academy.book.util.BookTestDataHelper;
import mate.academy.book.mapper.BookMapper;
import mate.academy.book.model.Book;
import mate.academy.book.repository.book.BookRepository;
import mate.academy.book.repository.book.BookSearchParametersMapper;
import mate.academy.book.repository.book.BookSpecificationProvider;
import mate.academy.book.service.book.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSearchParametersMapper parametersMapper;
    @Mock
    private BookSpecificationProvider bookSpecificationProvider;
    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @Test
    @DisplayName("Save valid book")
    void save_ValidBook_ReturnsValidBookDto() {
        Book book = new Book();
        CreateBookRequestDto bookRequestDto = BookTestDataHelper.createBookRequestDto();
        BookDto expected = BookTestDataHelper.createBookResponseDto();
        when(bookMapper.toModel(bookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book))
                .thenReturn(expected);
        BookDto actual = bookServiceImpl.save(bookRequestDto);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        verify(bookMapper, times(1)).toModel(bookRequestDto);
        verify(bookMapper, times(1)).toDto(book);
        verify(bookRepository, times(1)).save(book);
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("Find all books")
    void findAll_WhenBooksExist_ReturnsMappedBookPage() {
        Book book1 = new Book();
        Book book2 = new Book();
        Book book3 = new Book();
        List<Book> books = List.of(book1, book2, book3);
        PageRequest pageRequest = PageRequest.of(0, books.size());
        Page<Book> booksPage = new PageImpl<>(books, pageRequest, books.size());
        List<BookDto> bookDtos = BookTestDataHelper.createListOfBookDto();
        Page<BookDto> expected = new PageImpl<>(bookDtos, pageRequest, bookDtos.size());
        when(bookRepository.findAll(pageRequest)).thenReturn(booksPage);
        when(bookMapper.toDto(book1)).thenReturn(bookDtos.get(0));
        when(bookMapper.toDto(book2)).thenReturn(bookDtos.get(1));
        when(bookMapper.toDto(book3)).thenReturn(bookDtos.get(2));
        Page<BookDto> actual = bookServiceImpl.findAll(pageRequest);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        verify(bookRepository, times(1)).findAll(pageRequest);
        verify(bookMapper, times(3)).toDto(any(Book.class));
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }


    @Test
    @DisplayName("Get book with valid book id")
    void getBook_WithValidBookId_ReturnsValidBookDto() {
        Book book = new Book();
        BookDto expected = BookTestDataHelper.createBookResponseDto();
        when(bookRepository.getBookById(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookServiceImpl.getBookById(expected.getId());
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        verify(bookRepository, times(1)).getBookById(expected.getId());
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Throw exception if use invalid book id")
    void getBook_WithInvalidBookId_ThrowsException() {
        Long bookId = 25L;
        when(bookRepository.getBookById(bookId)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> bookServiceImpl.getBookById(bookId)
        );
        String expected = "Can't find book by id " + bookId;
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).getBookById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Delete book by valid book id")
    void deleteBook_WithValidBookId_DeletesBook() {
        Long bookId = 1L;
        bookServiceImpl.deleteBookById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Update book by book id")
    void updateBook_WithValidBookId_ReturnsUpdatedBookDto() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        CreateBookRequestDto bookRequestDto = BookTestDataHelper.createBookRequestDto();
        BookDto expected = BookTestDataHelper.createBookResponseDto();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookMapper).toUpdatedModel(book, bookRequestDto);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookServiceImpl.updateBookById(bookId, bookRequestDto);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toUpdatedModel(book, bookRequestDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Get all books by category id")
    void getBooksByCategoryId_WithValidId_ReturnsBookDtos() {
        Long categoryId = 1L;
        Book book = new Book();
        List<Book> books = List.of(book);
        List<BookDto> expected = List.of(BookTestDataHelper.createBookResponseDto());
        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(books);
        when(bookMapper.toDto(book)).thenReturn(expected.get(0));
        List<BookDto> actual = bookServiceImpl.getBooksByCategoryId(categoryId);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        verify(bookRepository, times(1)).findAllByCategoryId(categoryId);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Search books by specific params")
    @SuppressWarnings("unchecked")
    void searchBooks_WithValidParams_ReturnsBookDtos() {
        BookSearchParametersDto searchDto =
                new BookSearchParametersDto("book", "author");
        Map<String, String> paramsMap = Map.of(
                "title", "book",
                "author", "author"
        );
        Book book = new Book();
        List<Book> books = List.of(book);
        BookDto bookResponseDto = BookTestDataHelper.createBookResponseDto();
        List<BookDto> expected = List.of(bookResponseDto);
        Specification<Book> titleSpec = mock(Specification.class);
        Specification<Book> authorSpec = mock(Specification.class);
        when(parametersMapper.toMap(searchDto))
                .thenReturn(paramsMap);
        when(bookSpecificationProvider.getSpecification("title", "book"))
                .thenReturn(titleSpec);
        when(bookSpecificationProvider.getSpecification("author", "author"))
                .thenReturn(authorSpec);
        when(bookRepository.findAll(any(Specification.class)))
                .thenReturn(books);
        when(bookMapper.toDto(book))
                .thenReturn(bookResponseDto);
        List<BookDto> actual = bookServiceImpl.searchBooks(searchDto);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        verify(parametersMapper, times(1)).toMap(searchDto);
        verify(bookSpecificationProvider, times(1))
                .getSpecification("title", "book");
        verify(bookSpecificationProvider, times(1))
                .getSpecification("author", "author");
        verify(bookRepository, times(1))
                .findAll(any(Specification.class));
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(
                parametersMapper,
                bookSpecificationProvider,
                bookRepository,
                bookMapper
        );
    }
}
