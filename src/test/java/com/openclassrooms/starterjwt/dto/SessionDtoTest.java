package com.openclassrooms.starterjwt.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SessionDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;
    private static ObjectMapper objectMapper;

    private static String repeat(char c, int times) {
        char[] chars = new char[times];
        Arrays.fill(chars, c);
        return new String(chars);
    }

    @BeforeAll
    static void setup() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldNotValidateWhenNameIsBlank() {
        SessionDto dto = new SessionDto();
        dto.setName("   ");
        dto.setDate(new Date());
        dto.setTeacher_id(1L);
        dto.setDescription("Valid description");

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldNotValidateWhenDateIsNull() {
        SessionDto dto = new SessionDto();
        dto.setName("Valid Name");
        dto.setDate(null);
        dto.setTeacher_id(1L);
        dto.setDescription("Valid description");

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("date")));
    }

    @Test
    void shouldNotValidateWhenTeacherIdIsNull() {
        SessionDto dto = new SessionDto();
        dto.setName("Valid Name");
        dto.setDate(new Date());
        dto.setTeacher_id(null);
        dto.setDescription("Valid description");

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("teacher_id")));
    }

    @Test
    void shouldNotValidateWhenDescriptionIsTooLong() {
        SessionDto dto = new SessionDto();
        dto.setName("Valid Name");
        dto.setDate(new Date());
        dto.setTeacher_id(1L);
        dto.setDescription(repeat('x', 2600));

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(dto);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void shouldSerializeAndDeserializeSuccessfully() throws Exception {
        SessionDto dto = new SessionDto(
                10L,
                "Session Name",
                new Date(),
                5L,
                "Description",
                Arrays.asList(1L, 2L, 3L),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        String json = objectMapper.writeValueAsString(dto);
        assertNotNull(json);

        SessionDto deserialized = objectMapper.readValue(json, SessionDto.class);
        assertEquals(dto.getId(), deserialized.getId());
        assertEquals(dto.getName(), deserialized.getName());
        assertEquals(dto.getTeacher_id(), deserialized.getTeacher_id());
        assertEquals(dto.getDescription(), deserialized.getDescription());
        assertEquals(dto.getUsers(), deserialized.getUsers());
        assertEquals(dto.getCreatedAt(), deserialized.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), deserialized.getUpdatedAt());
    }

    @Test
    void shouldValidateSuccessfullyWithValidData() {
        SessionDto dto = new SessionDto();
        dto.setName("Valid Session");
        dto.setDate(new Date());
        dto.setTeacher_id(1L);
        dto.setDescription("A valid description");

        Set<ConstraintViolation<SessionDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @AfterAll
    static void tearDown() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        Date today = new Date();

        SessionDto dto1 = new SessionDto(1L, "Name", today, 1L, "Desc", Arrays.asList(1L, 2L), now, now);
        SessionDto dto2 = new SessionDto(1L, "Name", today, 1L, "Desc", Arrays.asList(1L, 2L), now, now);
        SessionDto dto3 = new SessionDto(2L, "Other", today, 2L, "Other Desc", Arrays.asList(3L), now, now);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());

        dto2.setName("Different");
        assertNotEquals(dto1, dto2);

        assertNotEquals(dto1, dto3);

        assertNotEquals(dto1, new Object());

        assertNotNull(dto1.toString());
        assertFalse(dto1.toString().isEmpty());
    }

}