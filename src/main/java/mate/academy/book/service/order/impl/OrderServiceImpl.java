package mate.academy.book.service.order.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.book.dto.order.OrderRequestDto;
import mate.academy.book.dto.order.OrderResponseDto;
import mate.academy.book.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.book.dto.orderitem.OrderItemResponseDto;
import mate.academy.book.exception.DataProcessingException;
import mate.academy.book.exception.EntityNotFoundException;
import mate.academy.book.mapper.ItemMapper;
import mate.academy.book.mapper.OrderItemMapper;
import mate.academy.book.mapper.OrderMapper;
import mate.academy.book.model.Book;
import mate.academy.book.model.CartItem;
import mate.academy.book.model.Order;
import mate.academy.book.model.OrderItem;
import mate.academy.book.model.ShoppingCart;
import mate.academy.book.model.User;
import mate.academy.book.repository.order.OrderRepository;
import mate.academy.book.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.book.repository.user.UserRepository;
import mate.academy.book.service.order.OrderService;
import mate.academy.book.service.shoppingcart.ShoppingCartService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderResponseDto completeOrder(String email, OrderRequestDto requestDto) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new DataProcessingException("Can't create oder, "
                    + "because shopping cart id empty");
        }
        Order order = orderMapper.toModel(requestDto);
        order.setUser(shoppingCart.getUser());
        order.setStatus(Order.Status.PENDING);
        order.setTotal(getTotalBooksPrice(email));
        order.setOrderDate(LocalDateTime.now());
        order.setOrderItems(
                getListOfOrderItemsFromListOfCartItems(email, order));
        shoppingCartService.clearShoppingCart(email);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderResponseDto> getOrderHistory(String email, Pageable pageable) {
        return orderRepository.findAllByUserId(getUserByEmail(email).getId(), pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public List<OrderItemResponseDto> getAllOrderItemsByOrderId(String email, Long orderId) {
        User user = getUserByEmail(email);
        List<OrderItem> orderItems = orderRepository.findAllOrderItemsByOrderIdAAndUserId(
                orderId, user.getId());
        if (orderItems.isEmpty()) {
            throw new DataProcessingException("Can't get order items by order id " + orderId);
        }
        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItemByIdAndOrderId(
            String email,
            Long orderItemId,
            Long orderId
    ) {
        List<OrderItemResponseDto> orderItemResponseDtos
                = getAllOrderItemsByOrderId(email, orderId);
        return orderItemResponseDtos.stream()
                .filter(orderItemResponseDto -> orderItemResponseDto.id().equals(orderItemId))
                .findFirst().orElseThrow(() -> new DataProcessingException(
                        "Can't get order item by id " + orderItemId));
    }

    @Override
    public OrderResponseDto updateOrderStatus(
            Long id,
            UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.getOrderById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't get order by id " + id));
        order.setStatus(requestDto.status());
        return orderMapper.toDto(order);
    }

    private User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by email " + email));
    }

    private ShoppingCart getShoppingCartByEmail(String email) {
        User user = getUserByEmail(email);
        return shoppingCartRepository
                .findShoppingCartByUserId(user.getId()).orElseThrow(() ->
                        new EntityNotFoundException("Can't find shopping cart "
                                + "by user with email " + user.getEmail()));
    }

    private BigDecimal getTotalBooksPrice(String email) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        return shoppingCart.getCartItems().stream()
                .map(CartItem::getBook)
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Set<OrderItem> getListOfOrderItemsFromListOfCartItems(
            String email, Order order) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> itemMapper.toOrderItem(cartItem, order))
                .collect(Collectors.toSet());
    }
}
