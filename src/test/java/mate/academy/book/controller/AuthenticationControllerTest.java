package mate.academy.book.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.book.dto.user.UserLoginRequestDto;
import mate.academy.book.dto.user.UserLoginResponseDto;
import mate.academy.book.dto.user.UserRegistrationRequestDto;
import mate.academy.book.dto.user.UserResponseDto;
import mate.academy.book.util.TestUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {
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

    @Test
    @DisplayName("Register user with valid request returns created user")
    @Sql(scripts = {
            "classpath:database/roles/add-one-role-to-roles-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void registerNewUser_WithValidRequestDto_Success() throws Exception {
        UserRegistrationRequestDto userRequestDto =
                TestUtil.createUserRequestDto();
        UserResponseDto expected = TestUtil.createUserResponseDto();
        String jsonRequest = objectMapper.writeValueAsString(userRequestDto);
        MvcResult result = mockMvc.perform(
                        post("/auth/registration")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andReturn();
        UserResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        UserResponseDto.class);
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isNotNull();
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Login with valid credentials returns token")
    @Sql(scripts = {
            "classpath:database/roles/add-one-role-to-roles-table.sql",
            "classpath:database/users/add-user-to-users-table.sql",
            "classpath:database/shoppingcarts/add-one-shoppingcart-to-shoppingcarts-table.sql",
            "classpath:database/usersroles/add-one-user-role-to-users-roles-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/usersroles/remove-users-roles-from-users-roles-table.sql",
            "classpath:database/shoppingcarts/remove-shoppingcarts-from-shoppingcarts-table.sql",
            "classpath:database/users/remove-users-from-users-table.sql",
            "classpath:database/roles/remove-roles-from-roles-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void login_ValidCredentials_ReturnsToken() throws Exception {
        UserLoginRequestDto request =
                TestUtil.createUserLoginRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(request);
        MvcResult result = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )
                .andExpect(status().isOk())
                .andReturn();
        UserLoginResponseDto response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                UserLoginResponseDto.class
        );
        assertThat(response).isNotNull();
        assertThat(response.token()).isNotBlank();
        assertThat(response.token().split("\\.")).hasSize(3);
    }
}
