package mate.academy.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import mate.academy.book.dto.cartitem.CartItemRequestDto;
import mate.academy.book.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.book.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.book.exception.EntityNotFoundException;
import mate.academy.book.mapper.CartItemMapper;
import mate.academy.book.mapper.ShoppingCartMapper;
import mate.academy.book.model.CartItem;
import mate.academy.book.model.ShoppingCart;
import mate.academy.book.model.User;
import mate.academy.book.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.book.repository.user.UserRepository;
import mate.academy.book.service.shoppingcart.impl.ShoppingCartServiceImpl;
import mate.academy.book.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartServiceImpl;

    @Test
    @DisplayName("Save valid shopping cart")
    void registerShoppingCart_userWithoutCart_savesShoppingCart() {
        User user = TestUtil.createDefaultUser();
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findShoppingCartByUserId(user.getId())).thenReturn(Optional.empty());
        shoppingCartServiceImpl.registerShoppingCard(user.getEmail());
        verify(shoppingCartRepository).save(
                argThat(cart -> cart.getUser().getId().equals(user.getId()))
        );
        verifyNoMoreInteractions(shoppingCartRepository);
    }

    @Test
    @DisplayName("Get shopping cart by user email")
    void getShoppingCart_WithValidUserEmail_ReturnsValidShoppingCartDto() {
        User user = TestUtil.createDefaultUser();
        ShoppingCart shoppingCart = new ShoppingCart().setUser(user);
        ShoppingCartResponseDto expected =
                TestUtil.createShoppingCartResponseDto();
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findShoppingCartByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);
        ShoppingCartResponseDto actual = shoppingCartServiceImpl.getShoppingCart(user.getEmail());
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        verify(userRepository).findUserByEmail(user.getEmail());
        verify(shoppingCartRepository).findShoppingCartByUserId(user.getId());
        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(userRepository, shoppingCartRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName("Throw exception when shopping cart not found for existing user")
    void getShoppingCart_WithNonExistingInDbUserEmail_ThrowsException() {
        User user = TestUtil.createDefaultUser();
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(shoppingCartRepository
                .findShoppingCartByUserId(user.getId())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartServiceImpl.getShoppingCart(user.getEmail())
        );
        String expected = "Can't find shopping cart by user with email " + user.getEmail();
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
        verify(userRepository).findUserByEmail(user.getEmail());
        verify(shoppingCartRepository)
                .findShoppingCartByUserId(user.getId());
        verifyNoMoreInteractions(shoppingCartRepository);
    }

    @Test
    @DisplayName("Delete cart item by user email and cart item id")
    void deleteCartItem_WithValidUserEmailAndCartItemId_DeletesCartItem() {
        User user = TestUtil.createDefaultUser();
        CartItem cartItem = TestUtil.createDefaultCartItem();
        Set<CartItem> cartItems =
                TestUtil.cartItemsWithSingleItem(cartItem);
        ShoppingCart shoppingCart =
                new ShoppingCart().setUser(user).setCartItems(cartItems);
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findShoppingCartByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        shoppingCartServiceImpl.deleteCartItem(user.getEmail(), cartItem.getId());
        assertThat(shoppingCart.getCartItems())
                .noneMatch(ci -> ci.getId().equals(cartItem.getId()));
        verify(userRepository).findUserByEmail(user.getEmail());
        verify(shoppingCartRepository)
                .findShoppingCartByUserId(user.getId());
        verifyNoMoreInteractions(userRepository, shoppingCartRepository);
    }

    @Test
    @DisplayName("Update quantity of books in shopping cart")
    void updateQuantityOfBook_WithNewCartItemQuantity_ReturnsUpdatedShoppingCartResponseDto() {
        User user = TestUtil.createDefaultUser();
        CartItem cartItem = TestUtil.createFirstCartItemWithFilledFields();
        Set<CartItem> cartItems =
                TestUtil.cartItemsWithSingleItem(cartItem);
        ShoppingCart shoppingCart = new ShoppingCart().setUser(user).setCartItems(cartItems);
        CartItemUpdateRequestDto cartItemUpdateRequestDto =
                TestUtil.createUpdatedCartItemRequestDto();
        ShoppingCartResponseDto expected =
                TestUtil.createShoppingCartResponseDtoWithUpdatedBooksQuantity();
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findShoppingCartByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);
        ShoppingCartResponseDto actual = shoppingCartServiceImpl.updateQuantityOfBookInShoppingCart(
                cartItem.getId(),
                user.getEmail(),
                cartItemUpdateRequestDto
        );
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        assertThat(cartItem.getQuantity())
                .isEqualTo(cartItemUpdateRequestDto.quantity());
        verify(userRepository).findUserByEmail(user.getEmail());
        verify(shoppingCartRepository)
                .findShoppingCartByUserId(user.getId());
        verify(shoppingCartRepository).save(shoppingCart);
        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(userRepository, shoppingCartRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName("Add new cart item when book not present in shopping cart")
    void addCartItem_ToShoppingCart_ReturnsUpdatedShoppingCartDto() {
        User user = TestUtil.createDefaultUser();
        CartItem firstCartItem =
                TestUtil.createFirstCartItemWithFilledFields();
        CartItem secondCartItem =
                TestUtil.createSecondCartItemWithFilledFields();
        Set<CartItem> cartItems =
                TestUtil.cartItemsWithSingleItem(firstCartItem);
        ShoppingCart shoppingCart = new ShoppingCart().setUser(user).setCartItems(cartItems);
        CartItemRequestDto cartItemRequestDto =
                TestUtil.createCartItemRequestDto();
        ShoppingCartResponseDto expected =
                TestUtil.createUpdatedShoppingCartResponseDtoWithTwoCartItems();
        when(userRepository.findUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findShoppingCartByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        when(cartItemMapper.toModel(cartItemRequestDto)).thenReturn(secondCartItem);
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);
        ShoppingCartResponseDto actual =
                shoppingCartServiceImpl.addCartItemToShoppingCart(user.getEmail(), cartItemRequestDto);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
        assertThat(secondCartItem.getQuantity())
                .isEqualTo(cartItemRequestDto.quantity());
        verify(userRepository, times(2)).findUserByEmail(user.getEmail());
        verify(shoppingCartRepository, times(2)).findShoppingCartByUserId(user.getId());
        verify(cartItemMapper).toModel(cartItemRequestDto);
        verify(shoppingCartRepository).save(shoppingCart);
        verify(shoppingCartRepository).save(argThat(cart -> cart.getCartItems().contains(secondCartItem)));
        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(userRepository, shoppingCartRepository,
                cartItemMapper, shoppingCartMapper);
    }
}
