package com.openclassrooms.starterjwt.models;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class TeacherModelTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testNoArgsConstructorAndSettersAndGetters() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        LocalDateTime now = LocalDateTime.now();
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getFirstName()).isEqualTo("John");
        assertThat(teacher.getLastName()).isEqualTo("Doe");
        assertThat(teacher.getCreatedAt()).isEqualTo(now);
        assertThat(teacher.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime created = LocalDateTime.of(2023, 1, 1, 12, 0);
        LocalDateTime updated = LocalDateTime.of(2023, 1, 2, 12, 0);

        Teacher teacher = new Teacher(1L, "Smith", "Anna", created, updated);

        assertThat(teacher.getId()).isEqualTo(1L);
        assertThat(teacher.getLastName()).isEqualTo("Smith");
        assertThat(teacher.getFirstName()).isEqualTo("Anna");
        assertThat(teacher.getCreatedAt()).isEqualTo(created);
        assertThat(teacher.getUpdatedAt()).isEqualTo(updated);
    }

    @Test
    void testBuilder() {
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        Teacher teacher = Teacher.builder()
                .id(10L)
                .firstName("Marie")
                .lastName("Curie")
                .createdAt(created)
                .updatedAt(updated)
                .build();

        assertThat(teacher.getId()).isEqualTo(10L);
        assertThat(teacher.getFirstName()).isEqualTo("Marie");
        assertThat(teacher.getLastName()).isEqualTo("Curie");
        assertThat(teacher.getCreatedAt()).isEqualTo(created);
        assertThat(teacher.getUpdatedAt()).isEqualTo(updated);
    }

    @Test
    void testEqualsAndHashCode() {
        Teacher t1 = Teacher.builder()
                .id(5L)
                .firstName("Albert")
                .lastName("Einstein")
                .build();

        Teacher t2 = Teacher.builder()
                .id(5L)
                .firstName("Albert")
                .lastName("Einstein")
                .build();

        Teacher t3 = Teacher.builder()
                .id(6L)
                .firstName("Isaac")
                .lastName("Newton")
                .build();

        assertThat(t1).isEqualTo(t2);
        assertThat(t1.hashCode()).isEqualTo(t2.hashCode());

        assertThat(t1).isNotEqualTo(t3);
        assertThat(t1.hashCode()).isNotEqualTo(t3.hashCode());
    }

    @Test
    void testToStringContainsFields() {
        Teacher teacher = Teacher.builder()
                .id(100L)
                .firstName("Ada")
                .lastName("Lovelace")
                .build();

        String toString = teacher.toString();

        assertThat(toString).contains("id=100");
        assertThat(toString).contains("firstName=Ada");
        assertThat(toString).contains("lastName=Lovelace");
    }

    @Test
    void whenFirstNameIsBlank_thenValidationFails() {
        TeacherDto dto = new TeacherDto(1L, "Doe", " ", null, null);
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void whenLastNameIsTooLong_thenValidationFails() {
        String tooLong = new String(new char[25]).replace('\0', 'a');
        TeacherDto dto = new TeacherDto(1L, tooLong, "John", null, null);
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(dto);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    void whenAllFieldsAreValid_thenValidationSucceeds() {
        TeacherDto dto = new TeacherDto(1L, "Doe", "John", null, null);
        Set<ConstraintViolation<TeacherDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }
}
