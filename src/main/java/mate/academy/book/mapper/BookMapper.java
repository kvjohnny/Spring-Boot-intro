package mate.academy.book.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.book.config.MapperConfig;
import mate.academy.book.dto.book.BookDto;
import mate.academy.book.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.book.dto.book.CreateBookRequestDto;
import mate.academy.book.model.Book;
import mate.academy.book.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> categoryIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoryIds(categoryIds);

    }

    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto dto) {
        Set<Category> categories = dto.getCategories().stream()
                .map(Category::new)
                .collect(Collectors.toSet());
        book.setCategories(categories);
    }

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Mapping(target = "categories", ignore = true)
    void toUpdatedModel(@MappingTarget Book book, CreateBookRequestDto requestDto);
}
