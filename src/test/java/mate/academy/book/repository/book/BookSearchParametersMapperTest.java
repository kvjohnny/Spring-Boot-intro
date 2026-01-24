package mate.academy.book.repository.book;

import java.util.Map;
import mate.academy.book.dto.book.BookSearchParametersDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BookSearchParametersMapperTest {
    private final BookSearchParametersMapper mapper =
            new BookSearchParametersMapper();

    @Test
    @DisplayName("Map all search parameters")
    void mapAll_GivenBookSearchParams_ReturnAllBookParams() {
        BookSearchParametersDto parametersDto =
                new BookSearchParametersDto("book", "author");
        Map<String, String> actual = mapper.toMap(parametersDto);
        Map<String, String> expected = Map.of(
                "title", "book",
                "author", "author"
        );
        Assertions.assertEquals(expected, actual);
    }
}
