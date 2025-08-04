package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SignupRequestTest {

    private Validator validator;

    private static String repeatString(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWithValidFields() {
        SignupRequest request = new SignupRequest();
        request.setEmail("marilia@example.com");
        request.setFirstName("Marilia");
        request.setLastName("Garcia");
        request.setPassword("securePassword123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    void shouldFailValidationWithBlankEmail() {
        SignupRequest request = new SignupRequest();
        request.setEmail("");
        request.setFirstName("Marilia");
        request.setLastName("Garcia");
        request.setPassword("securePassword123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailValidationWithInvalidEmail() {
        SignupRequest request = new SignupRequest();
        request.setEmail("invalid-email");
        request.setFirstName("Marilia");
        request.setLastName("Garcia");
        request.setPassword("securePassword123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldFailValidationWithShortFirstName() {
        SignupRequest request = new SignupRequest();
        request.setEmail("marilia@example.com");
        request.setFirstName("Ma");
        request.setLastName("Garcia");
        request.setPassword("securePassword123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    void shouldFailValidationWithShortLastName() {
        SignupRequest request = new SignupRequest();
        request.setEmail("marilia@example.com");
        request.setFirstName("Marilia");
        request.setLastName("G");
        request.setPassword("securePassword123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void shouldFailValidationWithTooLongLastName() {
        SignupRequest request = new SignupRequest();
        request.setEmail("marilia@example.com");
        request.setFirstName("Marilia");
        request.setLastName(repeatString("g", 21));
        request.setPassword("securePassword123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void shouldFailValidationWithShortPassword() {
        SignupRequest request = new SignupRequest();
        request.setEmail("marilia@example.com");
        request.setFirstName("Marilia");
        request.setLastName("Garcia");
        request.setPassword("123");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void shouldFailValidationWithNullPassword() {
        SignupRequest request = new SignupRequest();
        request.setEmail("marilia@example.com");
        request.setFirstName("Marilia");
        request.setLastName("Garcia");
        request.setPassword(null);

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void shouldPassValidationWithMinAndMaxPasswordLength() {
        SignupRequest min = new SignupRequest();
        min.setEmail("a@example.com");
        min.setFirstName("Ana");
        min.setLastName("Silva");
        min.setPassword("123456");

        SignupRequest max = new SignupRequest();
        max.setEmail("b@example.com");
        max.setFirstName("Ana");
        max.setLastName("Silva");
        max.setPassword(repeatString("a", 40));

        assertTrue(validator.validate(min).isEmpty());
        assertTrue(validator.validate(max).isEmpty());
    }

    @Test
    void shouldValidateEqualsAndHashCodeWithEqualObjects() {
        SignupRequest r1 = new SignupRequest();
        r1.setEmail("a@example.com");
        r1.setFirstName("Ana");
        r1.setLastName("Silva");
        r1.setPassword("123456");

        SignupRequest r2 = new SignupRequest();
        r2.setEmail("a@example.com");
        r2.setFirstName("Ana");
        r2.setLastName("Silva");
        r2.setPassword("123456");

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithNullFields() {
        SignupRequest r1 = new SignupRequest();
        r1.setEmail(null);
        r1.setFirstName(null);
        r1.setLastName(null);
        r1.setPassword(null);

        SignupRequest r2 = new SignupRequest();
        r2.setEmail(null);
        r2.setFirstName(null);
        r2.setLastName(null);
        r2.setPassword(null);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void shouldReturnFalseEqualsWithPartialFieldDifference() {
        SignupRequest r1 = new SignupRequest();
        r1.setEmail("a@example.com");
        r1.setFirstName("Ana");
        r1.setLastName("Silva");
        r1.setPassword("123456");

        SignupRequest r2 = new SignupRequest();
        r2.setEmail("a@example.com");
        r2.setFirstName("Ana");
        r2.setLastName("Souza"); // different
        r2.setPassword("123456");

        assertNotEquals(r1, r2);
    }

    @Test
    void shouldReturnFalseEqualsWithNullAndNonNullFields() {
        SignupRequest r1 = new SignupRequest();
        r1.setEmail("a@example.com");
        r1.setFirstName("Ana");
        r1.setLastName(null);
        r1.setPassword("123456");

        SignupRequest r2 = new SignupRequest();
        r2.setEmail("a@example.com");
        r2.setFirstName("Ana");
        r2.setLastName("Silva");
        r2.setPassword("123456");

        assertNotEquals(r1, r2);
    }

    @Test
    void shouldHaveNonEmptyToString() {
        SignupRequest r = new SignupRequest();
        r.setEmail("email@example.com");
        r.setFirstName("First");
        r.setLastName("Last");
        r.setPassword("pass");

        String result = r.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("email"));
    }

    @Test
    void shouldReturnFalseEqualsWithDifferentClass() {
        SignupRequest r1 = new SignupRequest();
        assertNotEquals(r1, new Object());
    }

    @Test
    void shouldReturnFalseEqualsWithNull() {
        SignupRequest r1 = new SignupRequest();
        assertNotEquals(r1, null);
    }
}
