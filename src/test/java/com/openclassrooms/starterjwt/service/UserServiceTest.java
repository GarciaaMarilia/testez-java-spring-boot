package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        Long userId = 1L;
        User mockUser = User.builder()
                .id(userId)
                .email("mari@example.com")
                .firstName("Marília")
                .lastName("Garcia")
                .password("123456")
                .admin(false)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = userService.findById(userId);

        assertNotNull(result);
        assertEquals("Garcia", result.getLastName());
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_shouldReturnNull_whenUserDoesNotExist() {
        Long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        User result = userService.findById(userId);

        assertNull(result);
        verify(userRepository).findById(userId);
    }

    @Test
    void delete_shouldCallDeleteById() {
        Long userId = 3L;

        userService.delete(userId);

        verify(userRepository).deleteById(userId);
    }
}
