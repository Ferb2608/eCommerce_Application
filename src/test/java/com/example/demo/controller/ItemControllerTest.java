package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);
    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
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

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findByName("Item 1"))
                .thenReturn(items.stream().filter(i -> i.getName().equals("Item 1"))
                        .collect(Collectors.toList()));
        when(itemRepository.findAll()).thenReturn(items);
    }
    @Test
    public void findItemById(){
        ResponseEntity<Item> response = itemController.getItemById(1L);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
    }
    @Test
    public void findItemByName(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item 1");
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(1, response.getBody().size());
    }
    @Test
    public void findAllItems(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertEquals(2, response.getBody().size());
    }
    @Test
    public void findItemByNameNotFound(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item 3");
        Assert.assertNull(response.getBody());
        Assert.assertEquals(404, response.getStatusCodeValue());
    }
}
