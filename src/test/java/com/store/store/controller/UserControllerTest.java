package com.store.store.controller;

import com.store.store.DTO.UserDto;
import com.store.store.model.User;
import com.store.store.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class UserControllerTest {

    private final UserService userService = Mockito.mock(UserService.class);
    private final UserController userController = new UserController(userService);

    @Test
    void testRegisterUser() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password123");
        userDto.setEmail("testuser@example.com");

        User registeredUser = new User();
        registeredUser.setId(1L);
        registeredUser.setUsername("testuser");
        registeredUser.setEmail("testuser@example.com");

        Mockito.when(userService.registerNewUser(any(UserDto.class))).thenReturn(registeredUser);

        // Act
        ResponseEntity<User> response = userController.registerUser(userDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        List<User> users = Arrays.asList(user1, user2);

        Mockito.when(userService.findAllUsers()).thenReturn(users);

        // Act
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("user1", response.getBody().get(0).getUsername());
    }

    @Test
    void testGetUserByUsername() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        Mockito.when(userService.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<User> response = userController.getUserByUsername("testuser");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    void testUpdateUser() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setUsername("updateduser");
        userDto.setEmail("updated@example.com");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updateduser");
        updatedUser.setEmail("updated@example.com");

        Mockito.when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(updatedUser);

        // Act
        ResponseEntity<User> response = userController.updateUser(1L, userDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("updateduser", response.getBody().getUsername());
    }

    @Test
    void testDeleteUser() {
        // Arrange
        Mockito.doNothing().when(userService).deleteUser(1L);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
