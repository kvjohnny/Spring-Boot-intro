package mate.academy.book.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.book.dto.cartitem.CartItemRequestDto;
import mate.academy.book.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.book.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.book.util.TestUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = {
        "classpath:database/roles/add-one-role-to-roles-table.sql",
        "classpath:database/users/add-user-to-users-table.sql",
        "classpath:database/usersroles/add-one-user-role-to-users-roles-table.sql",
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {
        "classpath:database/usersroles/remove-users-roles-from-users-roles-table.sql",
        "classpath:database/users/remove-users-from-users-table.sql",
        "classpath:database/roles/remove-roles-from-roles-table.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@Sql(scripts = {
        "classpath:database/books/add-three-books-to-books-table.sql",
        "classpath:database/shoppingcarts/add-one-shoppingcart-to-shoppingcarts-table.sql",
        "classpath:database/cartitems/add-one-cartitem-to-cartitems-table.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/cartitems/remove-cartitems-from-cartitems-table.sql",
        "classpath:database/shoppingcarts/remove-shoppingcarts-from-shoppingcarts-table.sql",
        "classpath:database/books/remove-books-from-books-table.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
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

    @WithUserDetails(value = "example@gmail.com")
    @Test
    @DisplayName("Get shopping cart")
    void get_ShoppingCart_ReturnsValidShoppingCart() throws Exception {
        ShoppingCartResponseDto expected =
                TestUtil.createShoppingCartResponseDto();
        MvcResult result = mockMvc.perform(
                        get("/cart")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual =
                objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        ShoppingCartResponseDto.class
                );
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @WithUserDetails(value = "example@gmail.com")
    @Test
    @DisplayName("Add cart item to shopping cart")
    void add_ValidCartItemToShoppingCart_ReturnsValidShoppingCart() throws Exception {
        CartItemRequestDto cartItemRequestDto = TestUtil.createCartItemRequestDto();
        ShoppingCartResponseDto expected =
                TestUtil.createUpdatedShoppingCartResponseDtoWithTwoCartItems();
        String jsonRequest = objectMapper.writeValueAsString(cartItemRequestDto);
        MvcResult result = mockMvc.perform(
                        post("/cart")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();
        ShoppingCartResponseDto actual =
                objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        ShoppingCartResponseDto.class
                );
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @WithUserDetails(value = "example@gmail.com")
    @Test
    @DisplayName("Update quantity of books in shopping cart")
    void update_QuantityOfBookInShoppingCart_ReturnsUpdatedShoppingCart() throws Exception {
        ShoppingCartResponseDto expected =
                TestUtil.createShoppingCartResponseDtoWithUpdatedBooksQuantity();
        CartItemUpdateRequestDto updatedCartItemRequestDto =
                TestUtil.createUpdatedCartItemRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(updatedCartItemRequestDto);
        MvcResult result = mockMvc.perform(
                        put("/cart/items/{cartItemId}", 1L)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual =
                objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        ShoppingCartResponseDto.class
                );
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @WithUserDetails(value = "example@gmail.com")
    @Test
    @DisplayName("Delete cart item by cart item id")
    void deleteCartItem_WithValidCartItemId_DeletesCartItem() throws Exception {
        mockMvc.perform(
                delete("/cart/items/{cartItemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }
}
