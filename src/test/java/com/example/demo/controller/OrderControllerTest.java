package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        User user = new User();
        user.setId(1);
        user.setUsername("hieu");
        user.setPassword("123456");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        List<Item> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");
        item1.setDescription("Item best seller");
        item1.setPrice(BigDecimal.valueOf(2000));

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");
        item2.setDescription("Item lowest price");
        item2.setPrice(BigDecimal.valueOf(1000));

        items.add(item1);
        items.add(item2);

        cart.setItems(items);
        user.setCart(cart);

        when(userRepository.findByUsername("hieu")).thenReturn(user);
        when(userRepository.findByUsername("nobody")).thenReturn(null);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
    }

    @Test
    public void submitOrder(){
        ResponseEntity<UserOrder> response = orderController.submit("hieu");
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void submitOrderWithInvalidUser(){
        ResponseEntity<UserOrder> response = orderController.submit("nobody");
        Assert.assertNull(response.getBody());
        Assert.assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    public void getOrderForUser(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("hieu");
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
    }
    @Test
    public void getOrderForUserWithInvalidUser(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("nobody");
        Assert.assertNull(response.getBody());
        Assert.assertEquals(404, response.getStatusCodeValue());
    }
}
