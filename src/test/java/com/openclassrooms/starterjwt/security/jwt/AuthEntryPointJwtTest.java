package com.openclassrooms.starterjwt.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.Mockito;
import org.springframework.security.core.AuthenticationException;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthEntryPointJwtTest {

    @Test
    void commence_shouldReturnUnauthorizedJsonResponse() throws Exception {
        // Arrange
        AuthEntryPointJwt entryPoint = new AuthEntryPointJwt();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException authException = mock(AuthenticationException.class);

        when(request.getServletPath()).thenReturn("/api/test");
        when(authException.getMessage()).thenReturn("Test unauthorized access");

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ServletOutputStream outputStream = new ServletOutputStream() {
            @Override
            public void write(int b) {
                byteStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(javax.servlet.WriteListener listener) {
            }
        };

        when(response.getOutputStream()).thenReturn(outputStream);

        entryPoint.commence(request, response, authException);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> json = mapper.readValue(byteStream.toByteArray(), Map.class);

        assertThat(json.get("status")).isEqualTo(401);
        assertThat(json.get("error")).isEqualTo("Unauthorized");
        assertThat(json.get("message")).isEqualTo("Test unauthorized access");
        assertThat(json.get("path")).isEqualTo("/api/test");
    }
}
