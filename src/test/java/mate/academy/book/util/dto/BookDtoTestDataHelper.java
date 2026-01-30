package mate.academy.book.util.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import mate.academy.book.dto.book.BookDto;
import mate.academy.book.dto.book.CreateBookRequestDto;

public class BookDtoTestDataHelper {
    public static CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Book 1");
        requestDto.setAuthor("Author 1");
        requestDto.setIsbn("123456");
        requestDto.setPrice(BigDecimal.valueOf(25.21));
        requestDto.setDescription("First book");
        requestDto.setCoverImage("cover1.jpg");
        requestDto.setCategories(Set.of(1L));
        return requestDto;
    }

    public static BookDto createBookResponseDto() {
        BookDto bookDto = new BookDto();
        Long bookId = 1L;
        bookDto.setId(bookId);
        bookDto.setTitle("Book 1");
        bookDto.setAuthor("Author 1");
        bookDto.setIsbn("123456");
        bookDto.setPrice(BigDecimal.valueOf(25.21));
        bookDto.setDescription("First book");
        bookDto.setCoverImage("cover1.jpg");
        bookDto.setCategoryIds(Set.of(1L));
        return bookDto;
    }

    public static List<BookDto> createListOfBookDto() {
        BookDto bookDto1 = new BookDto();
        Long bookId = 1L;
        bookDto1.setId(bookId);
        bookDto1.setTitle("Book 1");
        bookDto1.setAuthor("Author 1");
        bookDto1.setIsbn("123456");
        bookDto1.setPrice(BigDecimal.valueOf(25.21));
        bookDto1.setDescription("First book");
        bookDto1.setCoverImage("cover1.jpg");
        bookDto1.setCategoryIds(Set.of(1L));

        BookDto bookDto2 = new BookDto();
        Long bookId2 = 2L;
        bookDto2.setId(bookId2);
        bookDto2.setTitle("Book 2");
        bookDto2.setAuthor("Author 2");
        bookDto2.setIsbn("123456-2");
        bookDto2.setPrice(BigDecimal.valueOf(10.15));
        bookDto2.setDescription("Second book");
        bookDto2.setCoverImage("cover2.jpg");
        bookDto2.setCategoryIds(Set.of(1L));

        BookDto bookDto3 = new BookDto();
        Long bookId3 = 3L;
        bookDto3.setId(bookId3);
        bookDto3.setTitle("Book 3");
        bookDto3.setAuthor("Author 3");
        bookDto3.setIsbn("123456-3");
        bookDto3.setPrice(BigDecimal.valueOf(50.36));
        bookDto3.setDescription("Third book");
        bookDto3.setCoverImage("cover3.jpg");
        bookDto3.setCategoryIds(Set.of(2L));

        List<BookDto> bookDtos = new ArrayList<>();
        bookDtos.add(bookDto1);
        bookDtos.add(bookDto2);
        bookDtos.add(bookDto3);
        return bookDtos;
    }

    public static List<BookDto> createListOfBookDtosWithSameCategoryId() {
        BookDto bookDto1 = new BookDto();
        Long bookId = 1L;
        bookDto1.setId(bookId);
        bookDto1.setTitle("Book 1");
        bookDto1.setAuthor("Author 1");
        bookDto1.setIsbn("123456");
        bookDto1.setPrice(BigDecimal.valueOf(25.21));
        bookDto1.setDescription("First book");
        bookDto1.setCoverImage("cover1.jpg");
        bookDto1.setCategoryIds(Set.of(1L));

        BookDto bookDto2 = new BookDto();
        Long bookId2 = 2L;
        bookDto2.setId(bookId2);
        bookDto2.setTitle("Book 2");
        bookDto2.setAuthor("Author 2");
        bookDto2.setIsbn("123456-2");
        bookDto2.setPrice(BigDecimal.valueOf(10.15));
        bookDto2.setDescription("Second book");
        bookDto2.setCoverImage("cover2.jpg");
        bookDto2.setCategoryIds(Set.of(1L));

        List<BookDto> bookDtos = new ArrayList<>();
        bookDtos.add(bookDto1);
        bookDtos.add(bookDto2);
        return bookDtos;
    }

    public static CreateBookRequestDto createUpdatedBookRequestDto() {
        CreateBookRequestDto updatedRequestDto = new CreateBookRequestDto();
        updatedRequestDto.setTitle("Updated book 1");
        updatedRequestDto.setAuthor("Updated author 1");
        updatedRequestDto.setIsbn("123456");
        updatedRequestDto.setPrice(BigDecimal.valueOf(10.73));
        updatedRequestDto.setDescription("Updated first book");
        updatedRequestDto.setCoverImage("updated_cover1.jpg");
        updatedRequestDto.setCategories(Set.of(1L));
        return updatedRequestDto;
    }

    public static BookDto createUpdatedBookResponseDto() {
        BookDto updatedResponseDto = new BookDto();
        updatedResponseDto.setId(1L);
        updatedResponseDto.setTitle("Updated book 1");
        updatedResponseDto.setAuthor("Updated author 1");
        updatedResponseDto.setIsbn("123456");
        updatedResponseDto.setPrice(BigDecimal.valueOf(10.73));
        updatedResponseDto.setDescription("Updated first book");
        updatedResponseDto.setCoverImage("updated_cover1.jpg");
        updatedResponseDto.setCategoryIds(Set.of(1L));
        return updatedResponseDto;
    }
}
