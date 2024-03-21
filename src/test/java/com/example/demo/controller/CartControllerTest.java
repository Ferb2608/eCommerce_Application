package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);

        //Set up User
        User user = new User();
        user.setId(1);
        user.setUsername("hieu");
        user.setPassword("123456");

        Cart cart = new Cart();
        user.setCart(cart);

        //Set up Item
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");
        item1.setDescription("Item best seller");
        item1.setPrice(BigDecimal.valueOf(2000));

        when(userRepository.findByUsername("hieu")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
    }
    @Test
    public void addCart(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("hieu");
        modifyCartRequest.setQuantity(5);
        modifyCartRequest.setItemId(1L);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(BigDecimal.valueOf(10000), response.getBody().getTotal());
    }

    @Test
    public void addCartWithInvalidUser(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("hieu1");
        modifyCartRequest.setQuantity(5);
        modifyCartRequest.setItemId(1L);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Assert.assertNull(response.getBody());
        Assert.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addCartWithInvalidItem(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("hieu");
        modifyCartRequest.setQuantity(5);
        modifyCartRequest.setItemId(0L);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Assert.assertNull(response.getBody());
        Assert.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeCart(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("hieu");
        modifyCartRequest.setQuantity(5);
        modifyCartRequest.setItemId(1L);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(BigDecimal.valueOf(10000), response.getBody().getTotal());

        modifyCartRequest.setQuantity(1);
        response = cartController.removeFromcart(modifyCartRequest);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(BigDecimal.valueOf(8000), response.getBody().getTotal());
    }
}
