package com.example.demo.controllerTests;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Assertions;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.example.demo.TestUtils;


import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static com.example.demo.TestUtils.*;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();

        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        when(userRepository.findByUsername("test")).thenReturn(createUser());
        when(itemRepository.findById(any())).thenReturn(Optional.of(createItem(1)));
    }

    @Test
    public void testAddToCart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1);
        request.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());

        Cart c = response.getBody();
        Assertions.assertNotNull(c);

        assertNotNull(c);
        assertEquals(BigDecimal.valueOf(2.99 * c.getItems().size()), c.getTotal());
    }

    @Test
    public void testRemoveFromCart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1);
        request.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());

        response = cartController.removeFromcart(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());

        Cart c = response.getBody();
        assertNotNull(c);
        assertEquals(BigDecimal.valueOf(2.99 * c.getItems().size()), c.getTotal());
    }

    @Test
    public void testInvalidUser() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1);
        request.setUsername("invalidUser");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(404, response.getStatusCodeValue());
        Assertions.assertNull(response.getBody());

        response = cartController.addTocart(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(404, response.getStatusCodeValue());
        Assertions.assertNull(response.getBody());
    }



}