package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("john.doe@example.com");
        user.setFirstName("John");

        userDto = new UserDto();
        userDto.setEmail("john.doe@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setId(1L);
        userDto.setAdmin(false);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("john.doe@example.com")
                .password("password")
                .authorities("USER")
                .build();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldReturnUserByIdSuccessfully() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        user.setLastName("Doe");
        user.setPassword("password");
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setAdmin(false);

        userDto = new UserDto(
                1L,
                "john.doe@example.com",
                "Doe",
                "John",
                false,
                "password",
                now,
                now
        );

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.admin").value(false))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").exists());
    }

    @Test
    void testUserDtoEqualityAndToString() {
        LocalDateTime now = LocalDateTime.now();

        UserDto user1 = new UserDto(
                1L,
                "john.doe@example.com",
                "John",
                "Doe",
                false,
                null,
                now,
                now
        );

        UserDto user2 = new UserDto(
                1L,
                "john.doe@example.com",
                "John",
                "Doe",
                false,
                null,
                now,
                now
        );

        // ✅ Testa equals()
        assertEquals(user1, user2);

        // ✅ Testa hashCode()
        assertEquals(user1.hashCode(), user2.hashCode());

        // ✅ Testa toString()
        String toString = user1.toString();
        assertTrue(toString.contains("john.doe@example.com"));
        assertTrue(toString.contains("John"));
        assertTrue(toString.contains("Doe"));
    }


    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        when(userService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenIdIsNotANumber_get() throws Exception {
        mockMvc.perform(get("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteUserSuccessfullyWhenAuthorized() throws Exception {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("john.doe@example.com")
                .password("password")
                .authorities("USER")
                .build();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(1L);
    }

    @Test
    void shouldReturnUnauthorizedWhenUserTriesToDeleteAnotherAccount() throws Exception {
        UserDetails otherUserDetails = org.springframework.security.core.userdetails.User
                .withUsername("other@example.com")
                .password("password")
                .authorities("USER")
                .build();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(otherUserDetails, null, otherUserDetails.getAuthorities());

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).delete(anyLong());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistingUser() throws Exception {
        when(userService.findById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenIdIsNotANumber_delete() throws Exception {
        mockMvc.perform(delete("/api/user/abc"))
                .andExpect(status().isBadRequest());
    }
}
