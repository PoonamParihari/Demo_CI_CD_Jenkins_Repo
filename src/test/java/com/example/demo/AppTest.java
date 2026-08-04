package com.example.demo;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {

    @Test
    public void testGreet() {
        assertEquals("Hello CI/CD Pipeline!", App.greet());
    }
}
