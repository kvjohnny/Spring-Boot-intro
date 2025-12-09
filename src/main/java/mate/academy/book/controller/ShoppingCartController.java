package mate.academy.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.book.dto.cartitem.CartItemRequestDto;
import mate.academy.book.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.book.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.book.model.User;
import mate.academy.book.service.shoppingcart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/cart")
@RequiredArgsConstructor
@RestController
@Tag(name = "Shopping сart management", description = "Endpoints for managing shopping сarts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get shopping cart",
            description = "Get information about shopping cart")
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user.getEmail());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a cart item", description = "Add a cart item to the shopping cart")
    public ShoppingCartResponseDto addCartItemToShoppingCart(
            Authentication authentication, @RequestBody @Valid CartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService
                .addCartItemToShoppingCart(user.getEmail(), requestDto);
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update the quantity of book",
            description = "Update the quantity of book in the shopping cart")
    public ShoppingCartResponseDto updateQuantityOfBookInShoppingCart(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemUpdateRequestDto requestDto
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService
                .updateQuantityOfBookInShoppingCart(cartItemId, user.getEmail(), requestDto);
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Delete cart item by id", description = "Delete cart item by id")
    public void deleteCartItem(Authentication authentication, @PathVariable Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.deleteCartItem(user.getEmail(), cartItemId);
    }
}
