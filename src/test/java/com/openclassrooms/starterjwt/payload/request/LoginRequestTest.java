package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LoginRequestTest {

    private Validator validator;


    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWhenFieldsAreValid() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("password123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty(), "Deve passar sem nenhuma violação");
    }

    @Test
    void shouldFailValidationWhenEmailIsBlank() {
        LoginRequest request = new LoginRequest();
        request.setEmail("");
        request.setPassword("password123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailValidationWhenPasswordIsBlank() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword(" ");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void shouldFailValidationWhenAllFieldsAreBlank() {
        LoginRequest request = new LoginRequest();
        request.setEmail(" ");
        request.setPassword("");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertEquals(2, violations.size());
    }

    @Test
    void testGettersAndSetters() {
        LoginRequest req = new LoginRequest();
        req.setEmail("test@example.com");
        req.setPassword("123456");

        assertEquals("test@example.com", req.getEmail());
        assertEquals("123456", req.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        LoginRequest req1 = new LoginRequest();
        req1.setEmail("email@example.com");
        req1.setPassword("pass");

        LoginRequest req2 = new LoginRequest();
        req2.setEmail("email@example.com");
        req2.setPassword("pass");

        LoginRequest req3 = new LoginRequest();
        req3.setEmail("other@example.com");
        req3.setPassword("pass");

        assertEquals(req1.hashCode(), req2.hashCode());

        assertNotEquals(req1, req3);

        assertNotEquals(req1, null);

        assertNotEquals(req1, new Object());
    }


    @Test
    void testToStringNotEmpty() {
        LoginRequest req = new LoginRequest();
        req.setEmail("email@example.com");
        req.setPassword("pass");

        String s = req.toString();
        assertNotNull(s);
        assertFalse(s.isEmpty());
        assertTrue(s.contains("email") || s.contains("password"));
    }
}
