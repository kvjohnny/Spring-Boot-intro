package mate.academy.book.util.dto;

import java.util.ArrayList;
import java.util.List;
import mate.academy.book.dto.category.CategoryRequestDto;
import mate.academy.book.dto.category.CategoryResponseDto;

public class CategoryDtoTestDataHelper {
    public static CategoryRequestDto createCategoryRequestDto() {
        return new CategoryRequestDto("Fantasy", "Fantasy books");
    }

    public static CategoryResponseDto createCategoryResponseDto() {
        return new CategoryResponseDto(1L, "Fantasy", "Fantasy books");
    }

    public static List<CategoryResponseDto> createListOfCategoryResponseDtos() {
        CategoryResponseDto categoryResponseDto1 =
                new CategoryResponseDto(1L, "Fantasy", "Fantasy books");
        CategoryResponseDto categoryResponseDto2 =
                new CategoryResponseDto(2L, "Adventures", "Books about adventures");
        List<CategoryResponseDto> categoryResponseDtos = new ArrayList<>();
        categoryResponseDtos.add(categoryResponseDto1);
        categoryResponseDtos.add(categoryResponseDto2);
        return categoryResponseDtos;
    }

    public static CategoryRequestDto createUpdatedCategoryRequestDto() {
        return new CategoryRequestDto("Science", "Scientific books");
    }

    public static CategoryResponseDto createUpdatedCategoryResponseDto() {
        return new CategoryResponseDto(1L, "Science", "Scientific books");
    }
}
