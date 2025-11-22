package mate.academy.book.repository.book;

import java.util.HashMap;
import java.util.Map;
import mate.academy.book.dto.book.BookSearchParametersDto;
import org.springframework.stereotype.Component;

@Component
public class BookSearchParametersMapper {
    private static final String TITLE_KEY = "title";
    private static final String AUTHOR_KEY = "author";

    public Map<String, String> toMap(BookSearchParametersDto parameters) {
        Map<String, String> searchParams = new HashMap<>();
        if (parameters.title() != null && !parameters.title().isBlank()) {
            searchParams.put(TITLE_KEY, parameters.title());
        }
        if (parameters.author() != null && !parameters.author().isBlank()) {
            searchParams.put(AUTHOR_KEY, parameters.author());
        }
        return searchParams;
    }
}
