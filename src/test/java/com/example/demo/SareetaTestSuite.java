package com.example.demo;

import com.example.demo.controllerTests.CartControllerTest;
import com.example.demo.controllerTests.ItemControllerTest;
import com.example.demo.controllerTests.OrderControllerTest;
import com.example.demo.controllerTests.UserControllerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {CartControllerTest.class, UserControllerTest.class,
                ItemControllerTest.class, OrderControllerTest.class}
)
public class SareetaTestSuite {
}