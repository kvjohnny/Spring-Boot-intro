package mate.academy.book.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import mate.academy.book.dto.book.BookDto;
import mate.academy.book.dto.book.CreateBookRequestDto;
import mate.academy.book.factory.BookTestDataFactory;
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
public class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create a new book")
    @Sql(scripts = "classpath:database/categories/add-two-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/bookscategories/remove-books-categories-from-books-categories-table.sql",
            "classpath:database/categories/remove-categories-from-categories-table.sql",
            "classpath:database/books/remove-one-book-from-books-table.sql",
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = BookTestDataFactory.createBookRequestDto();
        BookDto expected = BookTestDataFactory.createBookResponseDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete book by valid book id")
    @Sql(scripts = {
            "classpath:database/books/add-one-book-to-books-table.sql",
            "classpath:database/categories/add-two-categories-table.sql",
            "classpath:database/bookscategories/add-one-book-category-to-books-categories-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/bookscategories/remove-books-categories-from-books-categories-table.sql",
            "classpath:database/categories/remove-categories-from-categories-table.sql",
            "classpath:database/books/remove-one-book-from-books-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBook_WithValidId_DeletesBook() throws Exception {
        mockMvc.perform(
                delete("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get all available books")
    @Sql(scripts = {
            "classpath:database/books/add-three-books-to-books-table.sql",
            "classpath:database/categories/add-two-categories-table.sql",
            "classpath:database/bookscategories/add-three-books-categories-to-books-categories-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/bookscategories/remove-books-categories-from-books-categories-table.sql",
            "classpath:database/categories/remove-categories-from-categories-table.sql",
            "classpath:database/books/remove-books-from-books-table.sql",
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_GivenBooks_ReturnsAllBooks() throws Exception {
        List<BookDto> expected = BookTestDataFactory.createListOfBookDto();
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode content = root.get("content");
        List<BookDto> actual = Arrays.asList(objectMapper.treeToValue(content, BookDto[].class));
        assertThat(actual).hasSize(3);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get book by valid book id")
    @Sql(scripts = {
            "classpath:database/books/add-one-book-to-books-table.sql",
            "classpath:database/categories/add-two-categories-table.sql",
            "classpath:database/bookscategories/add-one-book-category-to-books-categories-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/bookscategories/remove-books-categories-from-books-categories-table.sql",
            "classpath:database/categories/remove-categories-from-categories-table.sql",
            "classpath:database/books/remove-one-book-from-books-table.sql",
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBook_WithExistingBookId_ReturnsValidBook() throws Exception {
        BookDto expected = BookTestDataFactory.createBookResponseDto();
        MvcResult result = mockMvc.perform(
                        get("/books/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);

    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get book by invalid book id")
    void getBook_WithInvalidBookId_Success() throws Exception {
        mockMvc.perform(
                get("/books/{id}", 120L)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update book by book id")
    @Sql(scripts = {
            "classpath:database/books/add-one-book-to-books-table.sql",
            "classpath:database/categories/add-two-categories-table.sql",
            "classpath:database/bookscategories/add-one-book-category-to-books-categories-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/bookscategories/remove-books-categories-from-books-categories-table.sql",
            "classpath:database/categories/remove-categories-from-categories-table.sql",
            "classpath:database/books/remove-books-from-books-table.sql",
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_WithValidBookId_ReturnsUpdatedBook() throws Exception {
        BookDto expected = BookTestDataFactory.createUpdatedBookResponseDto();
        CreateBookRequestDto updatedBookRequestDto = BookTestDataFactory.createUpdatedBookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(updatedBookRequestDto);
        MvcResult result = mockMvc.perform(
                        put("/books/{id}", 1L)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        assertThat(actual).isNotNull();
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Get all books by specific params")
    @Sql(scripts = {
            "classpath:database/books/add-three-books-to-books-table.sql",
            "classpath:database/categories/add-two-categories-table.sql",
            "classpath:database/bookscategories/add-three-books-categories-to-books-categories-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/bookscategories/remove-books-categories-from-books-categories-table.sql",
            "classpath:database/categories/remove-categories-from-categories-table.sql",
            "classpath:database/books/remove-books-from-books-table.sql",
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void searchBooks_WhenTitleAndAuthorProvided_ReturnsMatchingBooks() throws Exception {
        List<BookDto> expected = BookTestDataFactory.createListOfBookDto();
        MvcResult result = mockMvc.perform(
                        get("/books/search")
                                .param("title", "book")
                                .param("author", "author")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        List<BookDto> actual = Arrays.asList(objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto[].class)
        );
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
