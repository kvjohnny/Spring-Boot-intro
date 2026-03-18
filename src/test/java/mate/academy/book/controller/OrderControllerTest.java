package mate.academy.book.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import mate.academy.book.dto.order.OrderRequestDto;
import mate.academy.book.dto.order.OrderResponseDto;
import mate.academy.book.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.book.dto.orderitem.OrderItemResponseDto;
import mate.academy.book.model.Order;
import mate.academy.book.util.TestUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {
        "classpath:database/roles/add-one-role-to-roles-table.sql",
        "classpath:database/users/add-user-to-users-table.sql",
        "classpath:database/usersroles/add-one-user-role-to-users-roles-table.sql",
        "classpath:database/books/add-one-book-to-books-table.sql",
        "classpath:database/orders/add-one-order-to-orders-table.sql",
        "classpath:database/orderitems/add-one-order-item-to-order-items-table.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/orderitems/remove-order-item-from-order-items-table.sql",
        "classpath:database/orders/remove-orders-from-orders-table.sql",
        "classpath:database/books/remove-books-from-books-table.sql",
        "classpath:database/usersroles/remove-users-roles-from-users-roles-table.sql",
        "classpath:database/users/remove-users-from-users-table.sql",
        "classpath:database/roles/remove-roles-from-roles-table.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderControllerTest {
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
    @DisplayName("Complete order")
    @Sql(scripts = {
            "classpath:database/roles/add-one-role-to-roles-table.sql",
            "classpath:database/users/add-user-to-users-table.sql",
            "classpath:database/usersroles/add-one-user-role-to-users-roles-table.sql",
            "classpath:database/books/add-one-book-to-books-table.sql",
            "classpath:database/shoppingcarts/add-one-shoppingcart-to-shoppingcarts-table.sql",
            "classpath:database/cartitems/add-one-cartitem-to-cartitems-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/orderitems/remove-order-item-from-order-items-table.sql",
            "classpath:database/orders/remove-orders-from-orders-table.sql",
            "classpath:database/cartitems/remove-cartitems-from-cartitems-table.sql",
            "classpath:database/shoppingcarts/remove-shoppingcarts-from-shoppingcarts-table.sql",
            "classpath:database/books/remove-books-from-books-table.sql",
            "classpath:database/usersroles/remove-users-roles-from-users-roles-table.sql",
            "classpath:database/users/remove-users-from-users-table.sql",
            "classpath:database/roles/remove-roles-from-roles-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void completeOrder_WhenShoppingCartIsNotEmpty_ReturnsOrder() throws Exception {
        OrderRequestDto orderRequestDto = TestUtil.createOrderRequestDto();
        OrderResponseDto expected = TestUtil.createOrderResponseDto();
        String jsonRequest = objectMapper.writeValueAsString(orderRequestDto);
        MvcResult result = mockMvc.perform(
                        post("/orders")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andReturn();
        OrderResponseDto actual = objectMapper.readValue(
                result.getResponse()
                        .getContentAsByteArray(),
                OrderResponseDto.class
        );
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isNotNull();
        assertThat(actual.total()).isPositive();
        assertThat(actual.orderItems()).isNotEmpty();
        assertThat(actual.status()).isEqualTo(Order.Status.PENDING);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderDate", "orderItems.id")
                .isEqualTo(expected);
    }

    @WithUserDetails(value = "example@gmail.com")
    @DisplayName("Get order history")
    @Test
    void getOrderHistory_WhenUserExists_ReturnsUserOrders() throws Exception {
        OrderResponseDto orderResponseDto =
                TestUtil.createOrderResponseDto();
        List<OrderResponseDto> expected = List.of(orderResponseDto);
        MvcResult result = mockMvc.perform(
                        get("/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode content = root.get("content");
        List<OrderResponseDto> actual =
                Arrays.asList(objectMapper.treeToValue(content, OrderResponseDto[].class));
        assertThat(actual).hasSize(1);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @WithUserDetails(value = "example@gmail.com")
    @Test
    @DisplayName("Get all order items by order id")
    void getAllOrderItemsByOrderId_WhenOrderExists_ReturnsOfOrderItems() throws Exception {
        OrderItemResponseDto orderItemDto = TestUtil.createDefaultOrderItemDto();
        List<OrderItemResponseDto> expected = List.of(orderItemDto);
        MvcResult result = mockMvc.perform(
                        get("/orders/{orderId}/items", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        List<OrderItemResponseDto> actual =
                Arrays.asList(
                        objectMapper.readValue(
                                result.getResponse().getContentAsByteArray(),
                                OrderItemResponseDto[].class
                        )
                );
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @WithUserDetails(value = "example@gmail.com")
    @Test
    @DisplayName("Get the order item by id and order id")
    void getOrderItemByIdAndOrderId_WhenOrderAndOrderItemExist_ReturnsOrderItem() throws Exception {
        OrderItemResponseDto expected = TestUtil.createDefaultOrderItemDto();
        MvcResult result = mockMvc.perform(
                        get("/orders/{orderId}/items/{id}", 1L, 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        OrderItemResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                OrderItemResponseDto.class
        );
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Update the order's status")
    void updateOrderStatus_WhenOrderExists_ReturnsUpdatedOrder() throws Exception {
        UpdateOrderStatusRequestDto updatedOrderStatusRequestDto =
                TestUtil.updateOrderStatusRequestDto();
        OrderResponseDto expected = TestUtil.updateOrderResponseDto();
        String jsonRequest = objectMapper.writeValueAsString(updatedOrderStatusRequestDto);
        MvcResult result = mockMvc.perform(
                        patch("/orders/{id}", 1L)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();
        OrderResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                OrderResponseDto.class
        );
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }
}
