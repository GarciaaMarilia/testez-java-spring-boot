package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    private JwtUtils jwtUtils;
    private final String jwtSecret = "testSecretKey1234567890";
    private final int jwtExpirationMs = 3600000;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtils = new JwtUtils();
        setPrivateField(jwtUtils, "jwtSecret", jwtSecret);
        setPrivateField(jwtUtils, "jwtExpirationMs", jwtExpirationMs);
    }

    @Test
    void testGenerateAndValidateJwtToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "testuser",
                "Test",
                "User",
                false,
                "password"
        );

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(token);

        boolean isValid = jwtUtils.validateJwtToken(token);
        assertTrue(isValid);

        String username = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals("testuser", username);
    }

    @Test
    void testValidateJwtToken_withExpiredToken() {
        String expiredToken = Jwts.builder()
                .setSubject("expireduser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 100000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(expiredToken);
        assertFalse(isValid);
    }

    @Test
    void testJwtTokenExpirationTiming() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                2L,
                "timinguser",
                "Time",
                "User",
                true,
                "password"
        );

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(token);

        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        Date expiration = claims.getExpiration();
        Date issuedAt = claims.getIssuedAt();

        long diff = expiration.getTime() - issuedAt.getTime();
        assertTrue(Math.abs(diff - jwtExpirationMs) < 1000);
    }

    @Test
    void testBuilderAndGetters() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(1L)
                .username("user1")
                .firstName("First")
                .lastName("Last")
                .admin(true)
                .password("secret")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("user1", user.getUsername());
        assertEquals("First", user.getFirstName());
        assertEquals("Last", user.getLastName());
        assertEquals(true, user.getAdmin());
        assertEquals("secret", user.getPassword());

        assertTrue(user.getAuthorities() instanceof HashSet);
        assertTrue(user.getAuthorities().isEmpty());

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

//    @Test
//    void testEqualsAndHashCode() {
//        UserDetailsImpl user1 = UserDetailsImpl.builder()
//                .id(1L)
//                .username("user1")
//                .build();
//
//        UserDetailsImpl user2 = UserDetailsImpl.builder()
//                .id(1L)
//                .username("user2")
//                .build();
//
//        UserDetailsImpl user3 = UserDetailsImpl.builder()
//                .id(2L)
//                .username("user3")
//                .build();
//
//        assertEquals(user1, user2);
//        assertEquals(user1.hashCode(), user2.hashCode());
//
//        assertNotEquals(user1, user3);
//
//        assertNotEquals(user1, null);
//        assertNotEquals(user1, new Object());
//    }

    @Test
    void testBuilderWithNullAdmin() {
        UserDetailsImpl user = UserDetailsImpl.builder()
                .id(5L)
                .username("user5")
                .admin(null)
                .build();

        assertNull(user.getAdmin());
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
