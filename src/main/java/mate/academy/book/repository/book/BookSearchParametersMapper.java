package mate.academy.book.repository.book;

import java.util.HashMap;
import java.util.Map;
import mate.academy.book.dto.BookSearchParametersDto;
import org.springframework.stereotype.Component;

@Component
public class BookSearchParametersMapper {
    public Map<String, String> toMap(BookSearchParametersDto parameters) {
        Map<String, String> searchParams = new HashMap<>();
        if (parameters.title() != null && !parameters.title().isBlank()) {
            searchParams.put("title", parameters.title());
        }
        if (parameters.author() != null && !parameters.author().isBlank()) {
            searchParams.put("author", parameters.author());
        }
        return searchParams;
    }
}
