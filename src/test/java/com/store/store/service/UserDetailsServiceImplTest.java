package com.store.store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.store.store.model.User;
import com.store.store.model.UserRole;
import com.store.store.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        // Setup mock user
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setPassword("encodedPassword");
        mockUser.setRole(UserRole.ROLE_USER);
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // Arrange
        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(mockUser));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals(mockUser.getUsername(), result.getUsername());
        assertEquals(mockUser.getPassword(), result.getPassword());
        assertEquals(1, result.getAuthorities().size());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(mockUser.getRole().name())));
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        String nonExistentUsername = "nonexistent";
        when(userRepository.findByUsername(nonExistentUsername))
                .thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(nonExistentUsername)
        );

        assertEquals("User not found with username: " + nonExistentUsername,
                exception.getMessage());
        verify(userRepository).findByUsername(nonExistentUsername);
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetailsWithCorrectAuthorities() {
        // Arrange
        mockUser.setRole(UserRole.ROLE_ADMIN);
        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(mockUser));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertEquals(1, result.getAuthorities().size());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetailsWithSellerRole() {
        // Arrange
        mockUser.setRole(UserRole.ROLE_SELLER);
        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(mockUser));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_SELLER")));
        assertEquals(1, result.getAuthorities().size());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_ShouldPreservePasswordEncoding() {
        // Arrange
        String encodedPassword = "$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG";
        mockUser.setPassword(encodedPassword);
        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(mockUser));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertEquals(encodedPassword, result.getPassword());
        verify(userRepository).findByUsername("testuser");
    }
}