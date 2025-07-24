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

public class TeacherDtoTest {

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

    private static String repeat(char c, int times) {
        char[] chars = new char[times];
        for (int i = 0; i < times; i++) {
            chars[i] = c;
        }
        return new String(chars);
    }

    @Test
    void shouldValidateCorrectTeacherDto() {
        TeacherDto dto = new TeacherDto(1L, "Garcia", "Marília", LocalDateTime.now(), LocalDateTime.now());
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Expected no validation violations");
    }

    @Test
    void shouldNotValidateWhenLastNameIsBlank() {
        TeacherDto dto = new TeacherDto();
        dto.setLastName("");
        dto.setFirstName("Marília");
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void shouldNotValidateWhenFirstNameIsBlank() {
        TeacherDto dto = new TeacherDto();
        dto.setLastName("Garcia");
        dto.setFirstName(" ");
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    void shouldNotValidateWhenLastNameIsTooLong() {
        TeacherDto dto = new TeacherDto();
        dto.setLastName(repeat('a', 21));
        dto.setFirstName("Marília");
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void shouldNotValidateWhenFirstNameIsTooLong() {
        TeacherDto dto = new TeacherDto();
        dto.setLastName("Garcia");
        dto.setFirstName(repeat('b', 21));
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    void testLombokMethods() {
        LocalDateTime now = LocalDateTime.now();

        TeacherDto dto1 = new TeacherDto(1L, "Silva", "Ana", now, now);
        TeacherDto dto2 = new TeacherDto(1L, "Silva", "Ana", now, now);
        TeacherDto dto3 = new TeacherDto(2L, "Santos", "Pedro", now, now);

        assertEquals("Silva", dto1.getLastName());
        assertEquals("Ana", dto1.getFirstName());

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        dto1.setLastName("Souza");
        assertNotEquals(dto1, dto2);

        assertNotEquals(dto1, dto3);

        assertNotEquals(dto1, new Object());

        assertNotNull(dto1.toString());
        assertFalse(dto1.toString().isEmpty());
    }


    @Test
    void testSettersForIdAndDates() {
        TeacherDto dto = new TeacherDto();

        dto.setId(10L);
        dto.setCreatedAt(LocalDateTime.of(2020, 1, 1, 10, 0));
        dto.setUpdatedAt(LocalDateTime.of(2020, 1, 2, 10, 0));

        assertEquals(10L, dto.getId());
        assertEquals(LocalDateTime.of(2020, 1, 1, 10, 0), dto.getCreatedAt());
        assertEquals(LocalDateTime.of(2020, 1, 2, 10, 0), dto.getUpdatedAt());
    }

}