package com.openclassrooms.starterjwt.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.models.User;

public class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByEmail_UserExists() {
        String email = "test@example.com";

        User user = new User();
        user.setEmail(email);
        user.setId(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userRepository.findByEmail(email);

        assertTrue(foundUser.isPresent());
        assertEquals(email, foundUser.get().getEmail());
    }

    @Test
    void testFindByEmail_UserDoesNotExist() {
        String email = "notfound@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepository.findByEmail(email);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testExistsByEmail_UserExists() {
        String email = "exists@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertTrue(userRepository.existsByEmail(email));
    }

    @Test
    void testExistsByEmail_UserDoesNotExist() {
        String email = "notexists@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertFalse(userRepository.existsByEmail(email));
    }
}

