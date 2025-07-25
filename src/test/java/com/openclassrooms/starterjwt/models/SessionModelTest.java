package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class SessionModelTest {

    @Test
    void testNoArgsConstructorAndSettersAndGetters() {
        Session session = new Session();
        session.setId(1L);
        session.setName("Session 1");
        Date now = new Date();
        session.setDate(now);
        session.setDescription("A description");
        Teacher teacher = new Teacher().setId(10L).setFirstName("John").setLastName("Doe");
        session.setTeacher(teacher);
        User user1 = new User().setId(100L).setEmail("user1@example.com").setFirstName("User").setLastName("One").setPassword("pwd").setAdmin(false);
        User user2 = new User().setId(101L).setEmail("user2@example.com").setFirstName("User").setLastName("Two").setPassword("pwd").setAdmin(false);
        session.setUsers(Arrays.asList(user1, user2));
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();
        session.setCreatedAt(created);
        session.setUpdatedAt(updated);

        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Session 1");
        assertThat(session.getDate()).isEqualTo(now);
        assertThat(session.getDescription()).isEqualTo("A description");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).containsExactly(user1, user2);
        assertThat(session.getCreatedAt()).isEqualTo(created);
        assertThat(session.getUpdatedAt()).isEqualTo(updated);
    }

    @Test
    void testAllArgsConstructor() {
        Date date = new Date();
        Teacher teacher = new Teacher(5L, "Last", "First", null, null);
        User user = new User(1L, "email@example.com", "LastName", "FirstName", "password", false, null, null);
        LocalDateTime created = LocalDateTime.now().minusDays(2);
        LocalDateTime updated = LocalDateTime.now().minusDays(1);

        Session session = new Session(1L, "Name", date, "Description", teacher, Arrays.asList(user), created, updated);

        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo("Name");
        assertThat(session.getDate()).isEqualTo(date);
        assertThat(session.getDescription()).isEqualTo("Description");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).containsExactly(user);
        assertThat(session.getCreatedAt()).isEqualTo(created);
        assertThat(session.getUpdatedAt()).isEqualTo(updated);
    }

    @Test
    void testBuilder() {
        Date date = new Date();
        Teacher teacher = new Teacher().setId(3L).setFirstName("Marie").setLastName("Curie");
        User user = new User().setId(20L).setEmail("test@example.com").setFirstName("Test").setLastName("User").setPassword("pwd").setAdmin(true);

        Session session = Session.builder()
                .id(2L)
                .name("SessionBuilder")
                .date(date)
                .description("Desc from builder")
                .teacher(teacher)
                .users(Arrays.asList(user))
                .build();

        assertThat(session.getId()).isEqualTo(2L);
        assertThat(session.getName()).isEqualTo("SessionBuilder");
        assertThat(session.getDate()).isEqualTo(date);
        assertThat(session.getDescription()).isEqualTo("Desc from builder");
        assertThat(session.getTeacher()).isEqualTo(teacher);
        assertThat(session.getUsers()).containsExactly(user);
    }

    @Test
    void testEqualsAndHashCode() {
        Date date = new Date();

        Session s1 = Session.builder()
                .id(1L)
                .name("A")
                .date(date)
                .description("desc")
                .build();

        Session s2 = Session.builder()
                .id(1L)
                .name("B")
                .date(date)
                .description("other desc")
                .build();

        Session s3 = Session.builder()
                .id(2L)
                .name("A")
                .date(date)
                .description("desc")
                .build();

        assertThat(s1).isEqualTo(s2);
        assertThat(s1.hashCode()).isEqualTo(s2.hashCode());

        assertThat(s1).isNotEqualTo(s3);
        assertThat(s1.hashCode()).isNotEqualTo(s3.hashCode());
    }

    @Test
    void testToStringContainsFields() {
        Session session = Session.builder()
                .id(100L)
                .name("SessionName")
                .date(new Date())
                .description("Session description")
                .build();

        String str = session.toString();

        assertThat(str).contains("id=100");
        assertThat(str).contains("name=SessionName");
        assertThat(str).contains("description=Session description");
    }

    @Test
    void testToStringAllFieldsCovered() {
        Date date = new Date();
        Teacher teacher = new Teacher().setId(1L).setFirstName("Ada").setLastName("Lovelace");
        User user = new User().setId(2L).setEmail("ada@example.com").setFirstName("Ada").setLastName("Lovelace").setPassword("pwd").setAdmin(true);
        LocalDateTime created = LocalDateTime.now().minusDays(1);
        LocalDateTime updated = LocalDateTime.now();

        Session session = Session.builder()
                .id(100L)
                .name("Complete Session")
                .date(date)
                .description("Full session")
                .teacher(teacher)
                .users(Arrays.asList(user))
                .createdAt(created)
                .updatedAt(updated)
                .build();

        String str = session.toString();

        assertThat(str).contains("id=100");
        assertThat(str).contains("name=Complete Session");
        assertThat(str).contains("description=Full session");
        assertThat(str).contains("teacher=Teacher");
        assertThat(str).contains("users=[User");
        assertThat(str).contains("createdAt=");
        assertThat(str).contains("updatedAt=");
    }

    @Test
    void testBuilderMethods() {
        Session.SessionBuilder builder = Session.builder();
        builder.id(1L);
        builder.name("name");
        builder.date(new Date());
        builder.description("desc");
        Session session = builder.build();
        assertThat(session).isNotNull();
    }


}
