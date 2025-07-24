package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SignupRequestTest {

    public static String repeatString(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWhenFieldsAreValid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("marilia@example.com");
        request.setFirstName("Marília");
        request.setLastName("Garcia");
        request.setPassword("securePassword123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Deve passar sem violação");
    }

    @Test
    void shouldFailWhenEmailIsBlank() {
        SignupRequest request = new SignupRequest();
        request.setEmail("");
        request.setFirstName("Marília");
        request.setLastName("Garcia");
        request.setPassword("securePassword123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailWhenEmailIsInvalid() {
        SignupRequest request = new SignupRequest();
        request.setEmail("invalid-email");
        request.setFirstName("Marília");
        request.setLastName("Garcia");
        request.setPassword("securePassword123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailWhenFirstNameIsTooShort() {
        SignupRequest request = new SignupRequest();
        request.setEmail("marilia@example.com");
        request.setFirstName("Ma");
        request.setLastName("Garcia");
        request.setPassword("securePassword123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    void shouldFailWhenLastNameIsTooLong() {
        String repeated = repeatString("g", 21);
        SignupRequest request = new SignupRequest();
        request.setEmail("marilia@example.com");
        request.setFirstName("Marília");
        request.setLastName(repeated);
        request.setPassword("securePassword123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void shouldFailWhenPasswordIsTooShort() {
        SignupRequest request = new SignupRequest();
        request.setEmail("marilia@example.com");
        request.setFirstName("Marília");
        request.setLastName("Garcia");
        request.setPassword("123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void testEqualsAndHashCode() {
        SignupRequest req1 = new SignupRequest();
        req1.setEmail("a@example.com");
        req1.setFirstName("Ana");
        req1.setLastName("Silva");
        req1.setPassword("123456");

        SignupRequest req2 = new SignupRequest();
        req2.setEmail("a@example.com");
        req2.setFirstName("Ana");
        req2.setLastName("Silva");
        req2.setPassword("123456");

        SignupRequest req3 = new SignupRequest();
        req3.setEmail("b@example.com");

        assertEquals(req1, req2);
        assertEquals(req1.hashCode(), req2.hashCode());

        assertNotEquals(req1, req3);

        assertNotEquals(req1, null);
        assertNotEquals(req1, new Object());
    }

    @Test
    void testToStringNotEmpty() {
        SignupRequest req = new SignupRequest();
        req.setEmail("email@example.com");
        req.setFirstName("First");
        req.setLastName("Last");
        req.setPassword("pass");

        String s = req.toString();
        assertNotNull(s);
        assertFalse(s.isEmpty());
        assertTrue(s.contains("email"));
    }

    @Test
    void testGetters() {
        SignupRequest req = new SignupRequest();
        req.setEmail("email@example.com");
        req.setFirstName("First");
        req.setLastName("Last");
        req.setPassword("pass");

        assertEquals("email@example.com", req.getEmail());
        assertEquals("First", req.getFirstName());
        assertEquals("Last", req.getLastName());
        assertEquals("pass", req.getPassword());
    }
}
