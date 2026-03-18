package mate.academy.book.util;

import mate.academy.book.dto.book.BookDto;
import mate.academy.book.dto.book.CreateBookRequestDto;
import mate.academy.book.dto.cartitem.CartItemRequestDto;
import mate.academy.book.dto.cartitem.CartItemResponseDto;
import mate.academy.book.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.book.dto.category.CategoryRequestDto;
import mate.academy.book.dto.category.CategoryResponseDto;
import mate.academy.book.dto.order.OrderRequestDto;
import mate.academy.book.dto.order.OrderResponseDto;
import mate.academy.book.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.book.dto.orderitem.OrderItemResponseDto;
import mate.academy.book.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.book.dto.user.UserLoginRequestDto;
import mate.academy.book.dto.user.UserRegistrationRequestDto;
import mate.academy.book.dto.user.UserResponseDto;
import mate.academy.book.model.Book;
import mate.academy.book.model.CartItem;
import mate.academy.book.model.Order;
import mate.academy.book.model.OrderItem;
import mate.academy.book.model.Role;
import mate.academy.book.model.ShoppingCart;
import mate.academy.book.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestUtil {
    public static CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Book 1");
        requestDto.setAuthor("Author 1");
        requestDto.setIsbn("123456");
        requestDto.setPrice(BigDecimal.valueOf(25.21));
        requestDto.setDescription("First book");
        requestDto.setCoverImage("cover1.jpg");
        requestDto.setCategories(Set.of(1L));
        return requestDto;
    }

    public static BookDto createBookResponseDto() {
        BookDto bookDto = new BookDto();
        Long bookId = 1L;
        bookDto.setId(bookId);
        bookDto.setTitle("Book 1");
        bookDto.setAuthor("Author 1");
        bookDto.setIsbn("123456");
        bookDto.setPrice(BigDecimal.valueOf(25.21));
        bookDto.setDescription("First book");
        bookDto.setCoverImage("cover1.jpg");
        bookDto.setCategoryIds(Set.of(1L));
        return bookDto;
    }

    public static List<BookDto> createListOfBookDto() {
        BookDto bookDto1 = new BookDto();
        Long bookId = 1L;
        bookDto1.setId(bookId);
        bookDto1.setTitle("Book 1");
        bookDto1.setAuthor("Author 1");
        bookDto1.setIsbn("123456");
        bookDto1.setPrice(BigDecimal.valueOf(25.21));
        bookDto1.setDescription("First book");
        bookDto1.setCoverImage("cover1.jpg");
        bookDto1.setCategoryIds(Set.of(1L));

        BookDto bookDto2 = new BookDto();
        Long bookId2 = 2L;
        bookDto2.setId(bookId2);
        bookDto2.setTitle("Book 2");
        bookDto2.setAuthor("Author 2");
        bookDto2.setIsbn("123456-2");
        bookDto2.setPrice(BigDecimal.valueOf(10.15));
        bookDto2.setDescription("Second book");
        bookDto2.setCoverImage("cover2.jpg");
        bookDto2.setCategoryIds(Set.of(1L));

        BookDto bookDto3 = new BookDto();
        Long bookId3 = 3L;
        bookDto3.setId(bookId3);
        bookDto3.setTitle("Book 3");
        bookDto3.setAuthor("Author 3");
        bookDto3.setIsbn("123456-3");
        bookDto3.setPrice(BigDecimal.valueOf(50.36));
        bookDto3.setDescription("Third book");
        bookDto3.setCoverImage("cover3.jpg");
        bookDto3.setCategoryIds(Set.of(2L));

        List<BookDto> bookDtos = new ArrayList<>();
        bookDtos.add(bookDto1);
        bookDtos.add(bookDto2);
        bookDtos.add(bookDto3);
        return bookDtos;
    }

    public static List<BookDto> createListOfBookDtosWithSameCategoryId() {
        BookDto bookDto1 = new BookDto();
        Long bookId = 1L;
        bookDto1.setId(bookId);
        bookDto1.setTitle("Book 1");
        bookDto1.setAuthor("Author 1");
        bookDto1.setIsbn("123456");
        bookDto1.setPrice(BigDecimal.valueOf(25.21));
        bookDto1.setDescription("First book");
        bookDto1.setCoverImage("cover1.jpg");
        bookDto1.setCategoryIds(Set.of(1L));

        BookDto bookDto2 = new BookDto();
        Long bookId2 = 2L;
        bookDto2.setId(bookId2);
        bookDto2.setTitle("Book 2");
        bookDto2.setAuthor("Author 2");
        bookDto2.setIsbn("123456-2");
        bookDto2.setPrice(BigDecimal.valueOf(10.15));
        bookDto2.setDescription("Second book");
        bookDto2.setCoverImage("cover2.jpg");
        bookDto2.setCategoryIds(Set.of(1L));

        List<BookDto> bookDtos = new ArrayList<>();
        bookDtos.add(bookDto1);
        bookDtos.add(bookDto2);
        return bookDtos;
    }

    public static CreateBookRequestDto createUpdatedBookRequestDto() {
        CreateBookRequestDto updatedRequestDto = new CreateBookRequestDto();
        updatedRequestDto.setTitle("Updated book 1");
        updatedRequestDto.setAuthor("Updated author 1");
        updatedRequestDto.setIsbn("123456");
        updatedRequestDto.setPrice(BigDecimal.valueOf(10.73));
        updatedRequestDto.setDescription("Updated first book");
        updatedRequestDto.setCoverImage("updated_cover1.jpg");
        updatedRequestDto.setCategories(Set.of(1L));
        return updatedRequestDto;
    }

    public static BookDto createUpdatedBookResponseDto() {
        BookDto updatedResponseDto = new BookDto();
        updatedResponseDto.setId(1L);
        updatedResponseDto.setTitle("Updated book 1");
        updatedResponseDto.setAuthor("Updated author 1");
        updatedResponseDto.setIsbn("123456");
        updatedResponseDto.setPrice(BigDecimal.valueOf(10.73));
        updatedResponseDto.setDescription("Updated first book");
        updatedResponseDto.setCoverImage("updated_cover1.jpg");
        updatedResponseDto.setCategoryIds(Set.of(1L));
        return updatedResponseDto;
    }

    public static CartItemUpdateRequestDto createUpdatedCartItemRequestDto() {
        return new CartItemUpdateRequestDto(10);
    }

    public static CartItemRequestDto createCartItemRequestDto() {
        return new CartItemRequestDto(2L, 5);
    }

    public static CartItem createDefaultCartItem() {
        return new CartItem().setId(1L);
    }

    public static CartItem createFirstCartItemWithFilledFields() {
        Book book = new Book()
                .setId(1L)
                .setTitle("Book 1")
                .setPrice(BigDecimal.valueOf(25.21));
        return new CartItem()
                .setId(1L)
                .setQuantity(4)
                .setBook(book);
    }

    public static CartItem createSecondCartItemWithFilledFields() {
        Book book = new Book().setId(2L).setTitle("Book 2");
        return new CartItem()
                .setId(2L)
                .setQuantity(5)
                .setBook(book);
    }

    public static Set<CartItem> cartItemsWithSingleItem(CartItem cartItem) {
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        return cartItems;
    }

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

    public static OrderRequestDto createOrderRequestDto() {
        return new OrderRequestDto("London");
    }

    public static OrderResponseDto createOrderResponseDto() {
        return new OrderResponseDto(
                1L,
                1L,
                Set.of(new OrderItemResponseDto(1L, 1L, 4)),
                LocalDateTime.of(2025, 12, 15, 19, 10, 44),
                BigDecimal.valueOf(100.84),
                Order.Status.PENDING
        );
    }

    public static OrderResponseDto updateOrderResponseDto() {
        return new OrderResponseDto(
                1L,
                1L,
                Set.of(new OrderItemResponseDto(1L, 1L, 4)),
                LocalDateTime.of(2025, 12, 15, 19, 10, 44),
                BigDecimal.valueOf(100.84),
                Order.Status.COMPLETED
        );
    }

    public static Order createDefaultOrder() {
        User user = TestUtil.createDefaultUserWithRole();
        Order order = new Order();
        Long orderId = 1L;
        order.setId(orderId);
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setTotal(BigDecimal.valueOf(100.84));
        order.setOrderDate(LocalDateTime.of(2025, 12, 15, 19, 10, 44));
        order.setShippingAddress("London");
        return order;
    }

    public static OrderItemResponseDto createDefaultOrderItemDto() {
        return new OrderItemResponseDto(1L, 1L, 4);
    }

    public static OrderItem createDefaultOrderItem() {
        Order order = TestUtil.createDefaultOrder();
        Long defaultId = 1L;
        Book book = new Book().setId(defaultId).setTitle("Book 1");
        OrderItem orderItem = new OrderItem();
        orderItem.setId(defaultId);
        orderItem.setOrder(order);
        orderItem.setBook(book);
        orderItem.setQuantity(4);
        orderItem.setPrice(BigDecimal.valueOf(25.21));
        return orderItem;
    }

    public static ShoppingCartResponseDto createShoppingCartResponseDto() {
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        CartItemResponseDto cartItemResponseDto =
                new CartItemResponseDto(1L, 1L, "Book 1", 4);
        shoppingCartResponseDto.setId(1L);
        shoppingCartResponseDto.setUserId(1L);
        shoppingCartResponseDto.setCartItems(Set.of(cartItemResponseDto));
        return shoppingCartResponseDto;
    }

    public static ShoppingCartResponseDto createShoppingCartResponseDtoWithUpdatedBooksQuantity() {
        ShoppingCartResponseDto cartResponseDto = new ShoppingCartResponseDto();
        CartItemResponseDto cartItemResponseDto =
                new CartItemResponseDto(1L, 1L, "Book 1", 10);
        cartResponseDto.setId(1L);
        cartResponseDto.setUserId(1L);
        cartResponseDto.setCartItems(Set.of(cartItemResponseDto));
        return cartResponseDto;
    }

    public static ShoppingCartResponseDto createUpdatedShoppingCartResponseDtoWithTwoCartItems() {
        ShoppingCartResponseDto cartResponseDto = new ShoppingCartResponseDto();
        CartItemResponseDto cartItemResponseDto1 =
                new CartItemResponseDto(1L, 1L, "Book 1", 4);
        cartResponseDto.setId(1L);
        cartResponseDto.setUserId(1L);
        CartItemResponseDto cartItemResponseDto2 =
                new CartItemResponseDto(2L, 2L, "Book 2", 5);
        cartResponseDto.setCartItems(Set.of(cartItemResponseDto1, cartItemResponseDto2));
        return cartResponseDto;
    }

    public static ShoppingCart createDefaultShoppingCart() {
        return new ShoppingCart();
    }

    public static UpdateOrderStatusRequestDto updateOrderStatusRequestDto() {
        return new UpdateOrderStatusRequestDto(Order.Status.COMPLETED);
    }

    public static UserRegistrationRequestDto createUserRequestDto() {
        UserRegistrationRequestDto userRequestDto = new UserRegistrationRequestDto();
        userRequestDto.setEmail("example@gmail.com");
        userRequestDto.setPassword("password");
        userRequestDto.setRepeatPassword("password");
        userRequestDto.setFirstName("Bob");
        userRequestDto.setLastName("Johnson");
        userRequestDto.setShippingAddress("London");
        return userRequestDto;
    }

    public static UserResponseDto createUserResponseDto() {
        return new UserResponseDto(
                1L,
                "example@gmail.com",
                "Bob",
                "Johnson",
                "London"
        );
    }

    public static UserLoginRequestDto createUserLoginRequestDto() {
        return new UserLoginRequestDto(
                "example@gmail.com",
                "password"
        );
    }

    public static User createDefaultUser() {
        User user = new User();
        Long userId = 1L;
        String email = "example@gmail.com";
        user.setId(userId);
        user.setEmail(email);
        return user;
    }

    public static User createDefaultUserWithRole() {
        User user = createDefaultUser();
        user.setPassword("password");
        user.setFirstName("Bob");
        user.setLastName("Johnson");
        user.setShippingAddress("London");
        Role role = new Role();
        role.setName(Role.RoleName.USER);
        user.setRoles(Set.of(role));
        return user;
    }
}
