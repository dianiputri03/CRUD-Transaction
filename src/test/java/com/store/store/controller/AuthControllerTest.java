package com.store.store.controller;

import com.store.store.DTO.LoginRequest;
import com.store.store.config.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private final String TEST_USERNAME = "testuser";
    private final String TEST_PASSWORD = "testpass";
    private final String TEST_TOKEN = "test.jwt.token";

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername(TEST_USERNAME);
        loginRequest.setPassword(TEST_PASSWORD);
    }

    @Test
    void login_SuccessfulAuthentication_ReturnsTokenInResponse() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(TEST_TOKEN);

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();
        assertEquals(TEST_TOKEN, responseBody.get("token"));
    }

    @Test
    void login_BadCredentials_ReturnsUnauthorized() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
    }

    @Test
    void login_AuthenticationError_ReturnsInternalServerError() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Authentication failed") {});

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Authentication failed", response.getBody());
    }
}