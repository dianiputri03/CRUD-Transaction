package com.store.store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.store.store.DTO.UserDto;
import com.store.store.model.User;
import com.store.store.model.UserRole;
import com.store.store.repository.UserRepository;
import com.store.store.exception.UserAlreadyExistsException;
import com.store.store.exception.InvalidRoleException;
import com.store.store.exception.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        // Setup UserDto
        userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setPassword("password123");
        userDto.setRole("ROLE_USER");

        // Setup User
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(UserRole.ROLE_USER);
        user.setEnabled(true);
    }

    @Test
    void registerNewUser_ShouldCreateNewUser_WhenValidData() {
        // Arrange
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.registerNewUser(userDto);

        // Assert
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(UserRole.ROLE_USER, result.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerNewUser_ShouldThrowException_WhenUsernameExists() {
        // Arrange
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerNewUser(userDto);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerNewUser_ShouldThrowException_WhenEmailExists() {
        // Arrange
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerNewUser(userDto);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerNewUser_ShouldThrowException_WhenInvalidRole() {
        // Arrange
        userDto.setRole("INVALID_ROLE");
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidRoleException.class, () -> {
            userService.registerNewUser(userDto);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findByUsername("testuser");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().getUsername());
    }

    @Test
    void findAllUsers_ShouldReturnListOfUsers() {
        // Arrange
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.findAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getUsername(), result.get(0).getUsername());
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenValidData() {
        // Arrange
        UserDto updateDto = new UserDto();
        updateDto.setUsername("newusername");
        updateDto.setEmail("newemail@example.com");
        updateDto.setPassword("newpassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(updateDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(updateDto.getPassword())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.updateUser(1L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(updateDto.getUsername(), result.getUsername());
        assertEquals(updateDto.getEmail(), result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(999L, userDto);
        });
    }

    @Test
    void updateUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        UserDto updateDto = new UserDto();
        updateDto.setEmail("existing@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(updateDto.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.updateUser(1L, updateDto);
        });
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(999L);
        });
        verify(userRepository, never()).delete(any(User.class));
    }
}