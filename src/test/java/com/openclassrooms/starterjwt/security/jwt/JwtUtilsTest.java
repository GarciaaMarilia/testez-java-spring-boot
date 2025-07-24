package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.lang.reflect.Field;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtils = new JwtUtils();

        injectField(jwtUtils, "jwtSecret", "testSecretKey");
        injectField(jwtUtils, "jwtExpirationMs", 3600000); // 1h
    }

    @Test
    void testGenerateAndValidateJwtToken() {
        UserDetailsImpl userDetails = new UserDetailsImpl(
                1L,
                "testuser",
                "test@test.com",
                "password",
                false,
                "0123456789"
        );

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String token = jwtUtils.generateJwtToken(auth);
        assertNotNull(token);

        assertTrue(jwtUtils.validateJwtToken(token));

        String username = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals("testuser", username);
    }

    @Test
    void testInvalidJwtToken() {
        String invalidToken = "invalid.token.value";
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }

    private void injectField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
