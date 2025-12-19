package mate.academy.book.service.shoppingcart.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.book.dto.cartitem.CartItemRequestDto;
import mate.academy.book.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.book.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.book.exception.DataProcessingException;
import mate.academy.book.exception.EntityNotFoundException;
import mate.academy.book.mapper.CartItemMapper;
import mate.academy.book.mapper.ShoppingCartMapper;
import mate.academy.book.model.CartItem;
import mate.academy.book.model.ShoppingCart;
import mate.academy.book.model.User;
import mate.academy.book.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.book.repository.user.UserRepository;
import mate.academy.book.service.shoppingcart.ShoppingCartService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartResponseDto getShoppingCart(String email) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto updateQuantityOfBookInShoppingCart(
            Long cartItemId, String email, CartItemUpdateRequestDto requestDto) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        CartItem cartItem = getCartItemById(shoppingCart, cartItemId);
        cartItem.setQuantity(requestDto.quantity());
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void deleteCartItem(String email, Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        CartItem cartItem = getCartItemById(shoppingCart, cartItemId);
        shoppingCart.getCartItems().remove(cartItem);
    }

    @Override
    public ShoppingCartResponseDto addCartItemToShoppingCart(
            String email, CartItemRequestDto requestDto) {
        if (isBookWithSameIdPresentInShoppingCart(email, requestDto)) {
            throw new DataProcessingException("The book with id " + requestDto.bookId()
                    + " is already present in the shopping cart. Please use the update option.");
        }
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void registerShoppingCard(String email) {
        User user = getUserByEmail(email);
        if (shoppingCartRepository.findShoppingCartByUserId(user.getId()).isPresent()) {
            throw new EntityNotFoundException("User by email " + user.getEmail()
                    + " has shopping cart yet.");
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by email " + email));
    }

    private ShoppingCart getShoppingCartByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("Can't find user by email " + email));
        return shoppingCartRepository
                .findShoppingCartByUserId(user.getId()).orElseThrow(() ->
                        new EntityNotFoundException("Can't find shopping cart "
                                + "by user with email " + user.getEmail()));
    }

    private boolean isBookWithSameIdPresentInShoppingCart(
            String email, CartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = getShoppingCartByEmail(email);
        return shoppingCart.getCartItems().stream()
                .anyMatch(cartItem ->
                        cartItem.getBook().getId().equals(requestDto.bookId()));
    }

    private CartItem getCartItemById(ShoppingCart shoppingCart, Long cartItemId) {
        return shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item by id "
                        + cartItemId));
    }
}
