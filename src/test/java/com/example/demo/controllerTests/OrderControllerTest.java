package com.example.demo.controllerTests;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.example.demo.TestUtils.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        User user = createUser();

        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.findByUser(any())).thenReturn(createOrders());
    }

    @Test
    public void testSubmitOrder() {
        ResponseEntity<UserOrder> response = orderController.submit("test");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        Assertions.assertEquals(createItems(), order.getItems());
        Assertions.assertEquals(createUser().getId(), order.getUser().getId());
    }

    @Test
    public void testGetOrdersForUser() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> orders = response.getBody();
        Assertions.assertEquals(createOrders().size(), orders.size());
    }

    @Test
    public void testSubmitOrderForInvalidUser(){
        ResponseEntity<UserOrder> response = orderController.submit("invalid");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(404, response.getStatusCodeValue());
        Assertions.assertNull( response.getBody());
    }

    @Test
    public void testGetOrdersForInvalidUser() {
        ResponseEntity<List<UserOrder>> ordersForUser = orderController.getOrdersForUser("invalid");
        assertNotNull(ordersForUser);
        assertEquals(404, ordersForUser.getStatusCodeValue());
    }



}