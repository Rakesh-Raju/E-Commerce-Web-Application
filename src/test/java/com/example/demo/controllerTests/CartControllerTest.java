package com.example.demo.controllerTests;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static com.example.demo.TestUtils.*;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setup(){

        when(userRepository.findByUsername("test")).thenReturn(createUser());
        when(itemRepository.findById(any())).thenReturn(Optional.of(createItem(1)));

    }

    @Test
    public void testAddToCart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(3);
        request.setItemId(1);
        request.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());

        Cart actualCart = response.getBody();
        Cart generatedCart = createCart(createUser());
        Assertions.assertNotNull(actualCart);

        Item item = createItem(request.getItemId());
        BigDecimal itemPrice = item.getPrice();
        BigDecimal expectedTotal = itemPrice.multiply(BigDecimal.valueOf(request.getQuantity())).add(generatedCart.getTotal());

        Assertions.assertEquals("test", actualCart.getUser().getUsername());
        Assertions.assertEquals(generatedCart.getItems().size() + request.getQuantity(), actualCart.getItems().size());
        Assertions.assertEquals(createItem(1), actualCart.getItems().get(0));
        Assertions.assertEquals(expectedTotal, actualCart.getTotal());

        verify(cartRepository, times(1)).save(actualCart);

    }

    @Test
    public void testRemoveFromCart(){

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1);
        request.setUsername("test");

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());

        Cart actualCart = response.getBody();

        Cart generatedCart = createCart(createUser());

        Assertions.assertNotNull(actualCart);

        Item item = createItem(request.getItemId());
        BigDecimal itemPrice = item.getPrice();

        BigDecimal expectedTotal = generatedCart.getTotal().subtract(itemPrice.multiply(BigDecimal.valueOf(request.getQuantity())));

        Assertions.assertEquals("test", actualCart.getUser().getUsername());
        Assertions.assertEquals(generatedCart.getItems().size() - request.getQuantity(), actualCart.getItems().size());
        Assertions.assertEquals(createItem(2), actualCart.getItems().get(0));
        Assertions.assertEquals(expectedTotal, actualCart.getTotal());

        verify(cartRepository, times(1)).save(actualCart);

    }

    @Test
    public void testInvalidUser(){

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(1);
        request.setItemId(1);
        request.setUsername("invalidUser");

        ResponseEntity<Cart> removeResponse = cartController.removeFromcart(request);
        Assertions.assertNotNull(removeResponse);
        Assertions.assertEquals(404, removeResponse.getStatusCodeValue());
        Assertions.assertNull(removeResponse.getBody());

        ResponseEntity<Cart> addResponse = cartController.addTocart(request);
        Assertions.assertNotNull(addResponse);
        Assertions.assertEquals(404, addResponse.getStatusCodeValue());
        Assertions.assertNull(addResponse.getBody());

        verify(userRepository, times(2)).findByUsername("invalidUser");

    }



}