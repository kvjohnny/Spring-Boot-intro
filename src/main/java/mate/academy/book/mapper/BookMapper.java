package mate.academy.book.mapper;

import mate.academy.book.config.MapperConfig;
import mate.academy.book.dto.BookDto;
import mate.academy.book.dto.CreateBookRequestDto;
import mate.academy.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void toUpdatedModel(@MappingTarget Book book, CreateBookRequestDto requestDto);
}
