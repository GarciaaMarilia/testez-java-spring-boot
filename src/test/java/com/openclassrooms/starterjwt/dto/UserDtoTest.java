package com.openclassrooms.starterjwt.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserDtoTest {

    private static Validator validator;
    private static ObjectMapper objectMapper;
    private static ValidatorFactory factory;

    @BeforeAll
    static void setup() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterAll
    static void tearDown() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void testLombokGettersSetters() {
        UserDto userDto = new UserDto();

        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setAdmin(true);
        userDto.setPassword("secret");
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());

        assertEquals(1L, userDto.getId());
        assertEquals("test@example.com", userDto.getEmail());
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
        assertTrue(userDto.isAdmin());
        assertEquals("secret", userDto.getPassword());
        assertNotNull(userDto.getCreatedAt());
        assertNotNull(userDto.getUpdatedAt());
    }

    @Test
    void testValidationConstraints_validData_noViolations() {
        UserDto userDto = new UserDto(
                1L,
                "valid.email@example.com",
                "Smith",
                "Jane",
                false,
                "password123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertTrue(violations.isEmpty(), "Expected no validation violations");
    }

    @Test
    void testValidationConstraints_invalidEmail_violationFound() {
        UserDto userDto = new UserDto();
        userDto.setEmail("invalid-email");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setAdmin(false);

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);

        assertFalse(violations.isEmpty());

        boolean hasEmailViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));

        assertTrue(hasEmailViolation);
    }

    @Test
    void testJsonIgnorePasswordOnSerialization() throws Exception {
        UserDto userDto = new UserDto(
                1L,
                "test@example.com",
                "Last",
                "First",
                false,
                "secretPassword",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        String json = objectMapper.writeValueAsString(userDto);

        assertFalse(json.contains("secretPassword"));
        assertFalse(json.contains("password"));
    }

    @Test
    void testEqualsAndCanEqual() {
        LocalDateTime now = LocalDateTime.now();

        UserDto user1 = new UserDto(1L, "email@example.com", "Last", "First", true, "pwd", now, now);
        UserDto user2 = new UserDto(1L, "email@example.com", "Last", "First", true, "pwd", now, now);
        UserDto user3 = new UserDto(2L, "other@example.com", "Other", "Name", false, "pwd2", now, now);

        assertEquals(user1, user1);

        assertEquals(user1, user2);
        assertTrue(user1.canEqual(user2));

        assertNotEquals(user1, user3);
        assertFalse(user1.equals(null));
        assertFalse(user1.equals("string"));
    }

    @Test
    void testHashCodeConsistency() {
        LocalDateTime now = LocalDateTime.now();

        UserDto user1 = new UserDto(1L, "email@example.com", "Last", "First", true, "pwd", now, now);
        UserDto user2 = new UserDto(1L, "email@example.com", "Last", "First", true, "pwd", now, now);

        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
