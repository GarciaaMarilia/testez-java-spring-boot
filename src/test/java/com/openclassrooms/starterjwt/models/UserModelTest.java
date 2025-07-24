package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class UserModelTest {

    @Test
    void testGettersAndSetters() {
        User user = new User();

        user.setId(1L)
                .setEmail("user@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setPassword("password123")
                .setAdmin(true)
                .setCreatedAt(LocalDateTime.of(2024, 7, 24, 12, 0))
                .setUpdatedAt(LocalDateTime.of(2024, 7, 25, 12, 0));

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("user@example.com");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.isAdmin()).isTrue();
        assertThat(user.getCreatedAt()).isEqualTo(LocalDateTime.of(2024, 7, 24, 12, 0));
        assertThat(user.getUpdatedAt()).isEqualTo(LocalDateTime.of(2024, 7, 25, 12, 0));
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(1L);
        User user3 = new User();
        user3.setId(2L);

        // users com mesmo id devem ser iguais
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());

        // usuários com ids diferentes são diferentes
        assertThat(user1).isNotEqualTo(user3);

        // equals null e tipo diferente
        assertThat(user1).isNotEqualTo(null);
        assertThat(user1).isNotEqualTo("uma string");
    }

    @Test
    void testToStringContainsFields() {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .admin(true)
                .createdAt(LocalDateTime.of(2024, 7, 24, 12, 0))
                .updatedAt(LocalDateTime.of(2024, 7, 25, 12, 0))
                .build();

        String toString = user.toString();

        assertThat(toString).contains("user@example.com");
        assertThat(toString).contains("John");
        assertThat(toString).contains("Doe");
        assertThat(toString).contains("true");  // admin
    }

    @Test
    void testBuilderCreatesCorrectUser() {
        User user = User.builder()
                .id(10L)
                .email("email@domain.com")
                .firstName("Alice")
                .lastName("Smith")
                .password("secret")
                .admin(false)
                .build();

        assertThat(user.getId()).isEqualTo(10L);
        assertThat(user.getEmail()).isEqualTo("email@domain.com");
        assertThat(user.getFirstName()).isEqualTo("Alice");
        assertThat(user.getLastName()).isEqualTo("Smith");
        assertThat(user.getPassword()).isEqualTo("secret");
        assertThat(user.isAdmin()).isFalse();
    }

    @Test
    void testRequiredArgsConstructor() {
        User user = new User("email@teste.com", "Last", "First", "pass123", true);

        assertThat(user.getEmail()).isEqualTo("email@teste.com");
        assertThat(user.getLastName()).isEqualTo("Last");
        assertThat(user.getFirstName()).isEqualTo("First");
        assertThat(user.getPassword()).isEqualTo("pass123");
        assertThat(user.isAdmin()).isTrue();
    }

}
