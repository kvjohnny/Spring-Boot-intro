package mate.academy.book.repository.shoppingcart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import mate.academy.book.model.ShoppingCart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Find shopping cart by user id")
    @Sql(scripts = {
            "classpath:database/users/add-user-to-users-table.sql",
            "classpath:database/shoppingcarts/add-one-shoppingcart-to-shoppingcarts-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/shoppingcarts/remove-shoppingcarts-from-shoppingcarts-table.sql",
            "classpath:database/users/remove-users-from-users-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findShoppingCartByUserId_WhenUserExists_ReturnsShoppingCart() {
        Long userId = 1L;
        Optional<ShoppingCart> optionalCart =
                shoppingCartRepository.findShoppingCartByUserId(userId);
        assertThat(optionalCart).isPresent();
        ShoppingCart shoppingCart = optionalCart.get();
        assertThat(shoppingCart.getId()).isNotNull();
        assertThat(shoppingCart.getUser().getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("ShoppingCart loads cartItems and books eagerly")
    @Sql(scripts = {
            "classpath:database/users/add-user-to-users-table.sql",
            "classpath:database/books/add-one-book-to-books-table.sql",
            "classpath:database/shoppingcarts/add-one-shoppingcart-to-shoppingcarts-table.sql",
            "classpath:database/cartitems/add-one-cartitem-to-cartitems-table.sql"

    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/cartitems/remove-cartitems-from-cartitems-table.sql",
            "classpath:database/shoppingcarts/remove-shoppingcarts-from-shoppingcarts-table.sql",
            "classpath:database/books/remove-books-from-books-table.sql",
            "classpath:database/users/remove-users-from-users-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findShoppingCartByUserId_LoadsCartItemsAndBooksEagerly() {
        ShoppingCart cart = shoppingCartRepository
                .findShoppingCartByUserId(1L)
                .orElseThrow();
        assertThat(cart.getCartItems()).isNotEmpty();
        cart.getCartItems().forEach(item ->
                assertThat(item.getBook()).isNotNull()
        );
        assertThat(cart.getCartItems()).hasSize(1);
    }
}
