package mate.academy.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.book.dto.category.CategoryRequestDto;
import mate.academy.book.dto.category.CategoryResponseDto;
import mate.academy.book.exception.EntityNotFoundException;
import mate.academy.book.util.dto.CategoryDtoTestDataHelper;
import mate.academy.book.mapper.CategoryMapper;
import mate.academy.book.model.Category;
import mate.academy.book.repository.category.CategoryRepository;
import mate.academy.book.service.category.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @Test
    @DisplayName("Get all available categories")
    void findAll_WhenCategoriesExist_ReturnsMappedCategoryPage() {
        Category category1 = new Category();
        Category category2 = new Category();
        List<Category> categories = List.of(category1, category2);
        PageRequest pageRequest = PageRequest.of(0, categories.size());
        Page<Category> categoryPage =
                new PageImpl<>(categories, pageRequest, categories.size());
        List<CategoryResponseDto> categoryDtos =
                CategoryDtoTestDataHelper.createListOfCategoryResponseDtos();
        Page<CategoryResponseDto> expected =
                new PageImpl<>(categoryDtos, pageRequest, categoryDtos.size());
        when(categoryRepository.findAll(pageRequest)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category1)).thenReturn(categoryDtos.get(0));
        when(categoryMapper.toDto(category2)).thenReturn(categoryDtos.get(1));
        Page<CategoryResponseDto> actual = categoryServiceImpl.findAll(pageRequest);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        verify(categoryRepository, times(1)).findAll(pageRequest);
        verify(categoryMapper, times(2)).toDto(any(Category.class));
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Find category by category id")
    void getCategory_WithExistingCategoryId_ReturnsCategoryDto() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        CategoryResponseDto expected = CategoryDtoTestDataHelper.createCategoryResponseDto();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryResponseDto actual = categoryServiceImpl.getById(categoryId);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Get category by invalid category id")
    void getById_WhenCategoryDoesNotExist_ThrowsException() {
        Long categoryId = 180L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryServiceImpl.getById(categoryId));
        String expected = "Can't find category by id " + categoryId;
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository, times(1)).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Save valid category")
    void save_WhenRequestDtoIsValid_ReturnsCategoryResponseDto() {
        Category category = new Category();
        CategoryRequestDto categoryRequestDto = CategoryDtoTestDataHelper.createCategoryRequestDto();
        CategoryResponseDto expected = CategoryDtoTestDataHelper.createCategoryResponseDto();
        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryResponseDto actual = categoryServiceImpl.save(categoryRequestDto);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        verify(categoryMapper, times(1)).toEntity(categoryRequestDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("Update category by category id")
    void update_WhenCategoryExists_ReturnsUpdatedCategoryResponseDto() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        CategoryRequestDto categoryRequestDto = CategoryDtoTestDataHelper.createCategoryRequestDto();
        CategoryResponseDto expected = CategoryDtoTestDataHelper.createCategoryResponseDto();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).toUpdatedModel(category, categoryRequestDto);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryResponseDto actual = categoryServiceImpl.update(categoryId, categoryRequestDto);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).toUpdatedModel(category, categoryRequestDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Delete category by category id")
    void deleteCategory_WhenCategoryExists_DeletesCategory() {
        Long categoryId = 1L;
        doNothing().when(categoryRepository).deleteCategoryById(categoryId);
        categoryServiceImpl.deleteCategoryById(categoryId);
        verify(categoryRepository, times(1)).deleteCategoryById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }
}
