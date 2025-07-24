package com.openclassrooms.starterjwt.payload.response;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class JwtResponseTest {

    @Test
    void testJwtResponseConstructorAndGetters() {
        String token = "dummyToken";
        Long id = 123L;
        String username = "user123";
        String firstName = "Marília";
        String lastName = "Garcia";
        Boolean admin = true;

        JwtResponse response = new JwtResponse(token, id, username, firstName, lastName, admin);

        assertEquals(token, response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals(id, response.getId());
        assertEquals(username, response.getUsername());
        assertEquals(firstName, response.getFirstName());
        assertEquals(lastName, response.getLastName());
        assertEquals(admin, response.getAdmin());
    }

    @Test
    void testSetters() {
        JwtResponse response = new JwtResponse("token", 1L, "user", "first", "last", false);

        response.setToken("newToken");
        response.setType("CustomType");
        response.setId(999L);
        response.setUsername("newUser");
        response.setFirstName("NewFirst");
        response.setLastName("NewLast");
        response.setAdmin(true);

        assertEquals("newToken", response.getToken());
        assertEquals("CustomType", response.getType());
        assertEquals(999L, response.getId());
        assertEquals("newUser", response.getUsername());
        assertEquals("NewFirst", response.getFirstName());
        assertEquals("NewLast", response.getLastName());
        assertTrue(response.getAdmin());
    }
}
