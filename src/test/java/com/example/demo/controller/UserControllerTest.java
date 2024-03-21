package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

        User user = new User();
        user.setId(1);
        user.setUsername("hieu");
        user.setPassword("123456");

        when(userRepository.findByUsername("hieu")).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
    }
    @Test
    public void testCreateUser(){
        when(bCryptPasswordEncoder.encode("123456")).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("hieu");
        createUserRequest.setPassword("123456");
        createUserRequest.setConfirmPassword("123456");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        Assert.assertEquals(createUserRequest.getUsername(), user.getUsername());
        Assert.assertEquals("hashedPassword", user.getPassword());
    }
    @Test
    public void findUserById(){
        User user = userController.findById(1L).getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(1, user.getId());
    }
    @Test
    public void findUserByName(){
        User user = userController.findByUserName("hieu").getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals("hieu", user.getUsername());
    }
    @Test
    public void inputWrongConfirmPassword(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("hieu");
        createUserRequest.setPassword("123456");
        createUserRequest.setConfirmPassword("1234567");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        Assert.assertNull(response.getBody());
        Assert.assertEquals(400, response.getStatusCodeValue());
    }
}
