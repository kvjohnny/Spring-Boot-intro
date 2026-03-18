package mate.academy.book.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import mate.academy.book.dto.book.BookDto;
import mate.academy.book.dto.category.CategoryRequestDto;
import mate.academy.book.dto.category.CategoryResponseDto;
import mate.academy.book.util.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext webApplicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new category")
    @Sql(scripts = "classpath:database/categories/remove-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_WithValidRequestDto_Success() throws Exception {
        CategoryRequestDto categoryRequestDto = TestUtil.createCategoryRequestDto();
        CategoryResponseDto expected = TestUtil.createCategoryResponseDto();
        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);
        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.id());
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get all available categories")
    @Sql(scripts = "classpath:database/categories/add-two-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ExistingCategories_ReturnsAllCategories() throws Exception {
        List<CategoryResponseDto> expected =
                TestUtil.createListOfCategoryResponseDtos();
        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        JsonNode content = root.get("content");
        List<CategoryResponseDto> actual =
                Arrays.asList(objectMapper.treeToValue(content, CategoryResponseDto[].class));
        assertThat(actual).hasSize(2);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get category by category id")
    @Sql(scripts = "classpath:database/categories/add-two-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategory_WithExistingCategoryId_ReturnsValidCategory() throws Exception {
        CategoryResponseDto expected = TestUtil.createCategoryResponseDto();
        MvcResult result = mockMvc.perform(
                        get("/categories/{id}", 1L)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class
        );
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update category by category id")
    @Sql(scripts = "classpath:database/categories/add-two-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_WithExistingId_ReturnsUpdatedCategory() throws Exception {
        CategoryRequestDto categoryRequestDto =
                TestUtil.createUpdatedCategoryRequestDto();
        CategoryResponseDto expected =
                TestUtil.createUpdatedCategoryResponseDto();
        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);
        MvcResult result = mockMvc.perform(
                        put("/categories/{id}", 1L)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class
        );
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get all books by category id")
    @Sql(scripts = {
            "classpath:database/categories/add-two-categories-table.sql",
            "classpath:database/books/add-three-books-to-books-table.sql",
            "classpath:database/bookscategories/"
                    + "add-three-books-categories-to-books-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/bookscategories/"
                    + "remove-books-categories-from-books-categories-table.sql",
            "classpath:database/books/remove-books-from-books-table.sql",
            "classpath:database/categories/remove-categories-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllBooks_WhenValidCategoryId_ReturnsBooksWithMatchingCategoryId() throws Exception {
        List<BookDto> expected =
                TestUtil.createListOfBookDtosWithSameCategoryId();
        MvcResult result = mockMvc.perform(
                        get("/categories/{id}/books", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> actual = Arrays.asList(objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), BookDto[].class));
        assertThat(actual).hasSize(2);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
