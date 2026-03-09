package mate.academy.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.book.dto.order.OrderRequestDto;
import mate.academy.book.dto.order.OrderResponseDto;
import mate.academy.book.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.book.dto.orderitem.OrderItemResponseDto;
import mate.academy.book.exception.DataProcessingException;
import mate.academy.book.mapper.ItemMapper;
import mate.academy.book.mapper.OrderItemMapper;
import mate.academy.book.mapper.OrderMapper;
import mate.academy.book.model.CartItem;
import mate.academy.book.model.Order;
import mate.academy.book.model.OrderItem;
import mate.academy.book.model.ShoppingCart;
import mate.academy.book.model.User;
import mate.academy.book.repository.order.OrderRepository;
import mate.academy.book.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.book.repository.user.UserRepository;
import mate.academy.book.service.order.impl.OrderServiceImpl;
import mate.academy.book.service.shoppingcart.ShoppingCartService;
import mate.academy.book.util.dto.OrderDtoTestDataHelper;
import mate.academy.book.util.dto.OrderItemDtoTestDataHelper;
import mate.academy.book.util.dto.UpdateOrderStatusDtoTestDataHelper;
import mate.academy.book.util.entity.CartItemTestDataHelper;
import mate.academy.book.util.entity.OrderItemTestDataHelper;
import mate.academy.book.util.entity.OrderTestDataHelper;
import mate.academy.book.util.entity.ShoppingCartTestDataHelper;
import mate.academy.book.util.entity.UserTestDataHelper;
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
public class OrderServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemMapper itemMapper;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @InjectMocks
    OrderServiceImpl orderServiceImpl;

    @Test
    @DisplayName("Complete order")
    void completeOrder_WhenShoppingCartIsNotEmpty_ReturnsOrderDto() {
        OrderRequestDto orderRequestDto = OrderDtoTestDataHelper.createOrderRequestDto();
        User user = UserTestDataHelper.createDefaultUserWithRole();
        ShoppingCart shoppingCart = ShoppingCartTestDataHelper.createDefaultShoppingCart();
        shoppingCart.setUser(user);
        CartItem cartItem = CartItemTestDataHelper.createFirstCartItemWithFilledFields();
        shoppingCart.setCartItems(Set.of(cartItem));
        Order order = OrderTestDataHelper.createDefaultOrder();
        OrderItem orderItem = OrderItemTestDataHelper.createDefaultOrderItem();
        OrderResponseDto expected = OrderDtoTestDataHelper.createOrderResponseDto();
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findShoppingCartByUserId(user.getId())).thenReturn(Optional.of(shoppingCart));
        when(orderMapper.toModel(orderRequestDto)).thenReturn(order);
        when(itemMapper.toOrderItem(cartItem, order)).thenReturn(orderItem);
        doNothing().when(shoppingCartService).clearShoppingCart(user.getEmail());
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(expected);
        OrderResponseDto actual = orderServiceImpl.completeOrder(user.getEmail(), orderRequestDto);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        verify(userRepository, times(1)).findUserByEmail(user.getEmail());
        verify(shoppingCartRepository, times(1)).findShoppingCartByUserId(user.getId());
        verify(orderMapper, times(1)).toModel(orderRequestDto);
        verify(itemMapper, times(1)).toOrderItem(cartItem, order);
        verify(shoppingCartService, times(1)).clearShoppingCart(user.getEmail());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderMapper, times(1)).toDto(order);
        verifyNoMoreInteractions(
                userRepository,
                shoppingCartRepository,
                itemMapper,
                orderRepository,
                orderMapper,
                shoppingCartService
        );
    }

    @Test
    @DisplayName("Throws DataProcessingException when shopping cart is empty")
    void completeOrder_WhenShoppingCartIsEmpty_ThrowsDataProcessingException() {
        OrderRequestDto orderRequestDto = OrderDtoTestDataHelper.createOrderRequestDto();
        User user = UserTestDataHelper.createDefaultUserWithRole();
        ShoppingCart shoppingCart = ShoppingCartTestDataHelper.createDefaultShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(Collections.emptySet());
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findShoppingCartByUserId(user.getId())).thenReturn(Optional.of(shoppingCart));
        DataProcessingException exception = assertThrows(DataProcessingException.class,
                () -> orderServiceImpl.completeOrder(user.getEmail(), orderRequestDto));
        String expected = "Can't create order, because shopping cart is empty";
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
        verify(userRepository, times(1)).findUserByEmail(user.getEmail());
        verify(shoppingCartRepository, times(1)).findShoppingCartByUserId(user.getId());
        verifyNoMoreInteractions(userRepository, shoppingCartRepository);
    }

    @Test
    @DisplayName("Get order history")
    void getOrderHistory_WhenUserExist_ReturnsPageOfOrderDtos() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Order order = OrderTestDataHelper.createDefaultOrder();
        List<Order> orders = List.of(order);
        Page<Order> orderPage = new PageImpl<>(orders, pageRequest, orders.size());
        User user = order.getUser();
        OrderResponseDto orderResponseDto = OrderDtoTestDataHelper.createOrderResponseDto();
        List<OrderResponseDto> orderResponseDtos = List.of(orderResponseDto);
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(orderRepository.findAllByUserId(user.getId(), pageRequest)).thenReturn(orderPage);
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);
        Page<OrderResponseDto> actual = orderServiceImpl.getOrderHistory(user.getEmail(), pageRequest);
        assertThat(actual.getContent()).isEqualTo(orderResponseDtos);
        assertThat(actual.getTotalElements()).isEqualTo(1);
        verify(userRepository, times(1)).findUserByEmail(user.getEmail());
        verify(orderRepository, times(1)).findAllByUserId(user.getId(), pageRequest);
        verify(orderMapper, times(1)).toDto(order);
        verifyNoMoreInteractions(userRepository, orderRepository, orderMapper);
    }

    @Test
    @DisplayName("Get all order items by order id")
    void getAllOrderItems_WhenOrderExists_ReturnsListOfOrderItemDtos() {
        OrderItem orderItem = OrderItemTestDataHelper.createDefaultOrderItem();
        Order order = orderItem.getOrder();
        User user = order.getUser();
        List<OrderItem> orderItems = List.of(orderItem);
        OrderItemResponseDto orderItemDto = OrderItemDtoTestDataHelper.createDefaultOrderItemDto();
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(orderRepository.findAllOrderItemsByOrderIdAndUserId(
                order.getId(), user.getId())).thenReturn(orderItems);
        when(orderItemMapper.toDto(orderItem)).thenReturn(orderItemDto);
        List<OrderItemResponseDto> actual =
                orderServiceImpl.getAllOrderItemsByOrderId(user.getEmail(), order.getId());
        assertThat(actual).containsExactly(orderItemDto);
        verify(userRepository, times(1)).findUserByEmail(user.getEmail());
        verify(orderRepository, times(1))
                .findAllOrderItemsByOrderIdAndUserId(order.getId(), user.getId());
        verify(orderItemMapper, times(1)).toDto(orderItem);
        verifyNoMoreInteractions(userRepository, orderRepository, orderItemMapper);
    }

    @Test
    @DisplayName("Throws DataProcessingException when order items are empty")
    void getAllOrderItems_WhenOrderItemsEmpty_ThrowsDataProcessingException() {
        Order order = OrderTestDataHelper.createDefaultOrder();
        User user = order.getUser();
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(orderRepository.findAllOrderItemsByOrderIdAndUserId(order.getId(), user.getId()))
                .thenReturn(Collections.emptyList());
        DataProcessingException exception = assertThrows(DataProcessingException.class,
                () -> orderServiceImpl.getAllOrderItemsByOrderId(user.getEmail(), order.getId()));
        String expected = "Can't get order items by order id " + order.getId();
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
        verify(userRepository, times(1)).findUserByEmail(user.getEmail());
        verify(orderRepository, times(1))
                .findAllOrderItemsByOrderIdAndUserId(order.getId(), user.getId());
        verifyNoMoreInteractions(userRepository, orderRepository);
    }

    @Test
    @DisplayName("Get order item by order item id and order id")
    void getOrderItemByIdAndOrderId_whenOrderItemExists_ReturnsOrderItemDto() {
        Long orderId = 1L;
        Long orderItemId = 1L;
        String email = "example@gmail.com";
        OrderItemResponseDto expected = OrderItemDtoTestDataHelper.createDefaultOrderItemDto();
        OrderServiceImpl spyOrderService = spy(orderServiceImpl);
        doReturn(List.of(expected))
                .when(spyOrderService)
                .getAllOrderItemsByOrderId(email, orderId);
        OrderItemResponseDto actual = spyOrderService
                .getOrderItemByIdAndOrderId(email, orderItemId, orderId);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Update order status by order id")
    void updateOrderStatus_WhenOrderExists_UpdatesStatusAndReturnsOrderDto() {
        Order order = OrderTestDataHelper.createDefaultOrder();
        UpdateOrderStatusRequestDto updatedOrderStatusRequestDto =
                UpdateOrderStatusDtoTestDataHelper.updateOrderStatusRequestDto();
        OrderResponseDto expected =
                OrderDtoTestDataHelper.updateOrderResponseDto();
        when(orderRepository.getOrderById(order.getId())).thenReturn(Optional.of(order));
        when(orderMapper.toDto(any(Order.class))).thenReturn(expected);
        OrderResponseDto actual = orderServiceImpl
                .updateOrderStatus(order.getId(), updatedOrderStatusRequestDto);
        assertThat(order.getStatus()).isEqualTo(updatedOrderStatusRequestDto.status());
        assertThat(actual).isEqualTo(expected);
        verify(orderRepository, times(1)).getOrderById(order.getId());
        verify(orderMapper, times(1))
                .toDto(argThat(o -> o.getStatus().equals(updatedOrderStatusRequestDto.status())));
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }
}
