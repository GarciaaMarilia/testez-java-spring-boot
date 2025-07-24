package com.openclassrooms.starterjwt.payload.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MessageResponseTest {

    @Test
    void testConstructorAndGetter() {
        String msg = "Hello, world!";
        MessageResponse response = new MessageResponse(msg);

        assertEquals(msg, response.getMessage());
    }

    @Test
    void testSetter() {
        MessageResponse response = new MessageResponse("Initial message");

        response.setMessage("Updated message");

        assertEquals("Updated message", response.getMessage());
    }
}

