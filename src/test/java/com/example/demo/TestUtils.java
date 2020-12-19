package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {

        boolean wasPrivate = false;

        try {

            Field field = target.getClass().getDeclaredField(fieldName);
            if(!field.canAccess(target)){
                field.setAccessible(true);
                wasPrivate = true;
            }

            field.set(target, toInject);
            if(wasPrivate){
                field.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

     /*
        Reusable calls
     */

    public static User createUser() {
        User user = new User();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(createCart(user));
        return user;
    }

    public static Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        cart.setItems(createItems());
        cart.setTotal(cart.getItems().stream().map(item -> item.getPrice()).reduce(BigDecimal::add).get());

        return cart;
    }

    public static List<Item> createItems() {

        List<Item> items = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            items.add(createItem(i));
        }

        return items;
    }

    public static Item createItem(long id){
        Item item = new Item();
        item.setId(id);
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setName("Item: " + item.getId());
        item.setDescription("Description: [default] ");
        return item;
    }

    public static List<UserOrder> createOrders(){
        List<UserOrder> orders = new ArrayList<>();
            for(int i = 0; i < 3; i++) {
                UserOrder order = new UserOrder();
                Cart cart = createCart(createUser());
                order.setItems(cart.getItems());
                order.setTotal(cart.getTotal());
                order.setUser(createUser());
                order.setId(Long.valueOf(i));
                orders.add(order);
            }
        return orders;
    }

}