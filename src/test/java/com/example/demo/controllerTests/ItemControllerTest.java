package com.example.demo.controllerTests;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import org.junit.jupiter.api.Assertions;

import static com.example.demo.TestUtils.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {


    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem(1)));
        when(itemRepository.findAll()).thenReturn(createItems());
        when(itemRepository.findByName("item")).thenReturn(Arrays.asList(createItem(1), createItem(2)));
    }

    @Test
    public void testGetItems() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        Assertions.assertEquals(createItems(), items);
    }

    @Test
    public void testGetItemById() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Item item = response.getBody();
        Assertions.assertEquals(createItem(1L), item);
    }

    @Test
    public void testGetInvalidItem() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("invalidItem");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(404, response.getStatusCodeValue());
        Assertions.assertNull(response.getBody());
    }

    @Test
    public void testGetItemByName() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("item");
        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());
        List<Item> items = Arrays.asList(createItem(1), createItem(2));
        Assertions.assertEquals(createItems(), items);
    }

    @Test
    public void testGetItemByNameInvalid() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("invalid");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }


}