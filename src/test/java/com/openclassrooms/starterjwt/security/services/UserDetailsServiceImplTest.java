package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    private UserRepository userRepository;
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void loadUserByUsername_whenUserExists_shouldReturnUserDetails() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email@example.com");
        user.setFirstName("Nome");
        user.setLastName("Sobrenome");
        user.setPassword("senha");
        user.setAdmin(false);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername("email@example.com");

        assertEquals(user.getEmail(), userDetails.getUsername());
        assertNotNull(userDetails.getAuthorities());
    }


    @Test
    void loadUserByUsername_whenUserDoesNotExist_shouldThrowException() {
        when(userRepository.findByEmail("notfound@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("notfound@example.com"));
    }
}
