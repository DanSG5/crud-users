package com.danseg.crudusuarios.controller;

import com.danseg.crudusuarios.exception.DataValidationException;
import com.danseg.crudusuarios.exception.UserNotFoundException;
import com.danseg.crudusuarios.model.User;
import com.danseg.crudusuarios.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setName("Dan");
        user.setLastName("Segovia");

        when(userService.saveUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.saveUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testFindUsers() {
        User user1 = new User();
        user1.setName("Dan");
        User user2 = new User();
        user2.setName("Angel");

        List<User> users = Arrays.asList(user1, user2);
        when(userService.findUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.findUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testFindUsers_NoContent() {
        when(userService.findUsers()).thenReturn(Arrays.asList());

        ResponseEntity<List<User>> response = userController.findUsers();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    public void testFindUserById() {
        User user = new User();
        user.setName("Dan");

        when(userService.findUserById(1L)).thenReturn(user);

        ResponseEntity<User> response = userController.findUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testFindUserById_NotFound() {
        when(userService.findUserById(1L)).thenThrow(new UserNotFoundException("Usuario con el ID 1 no fue encontrado"));

        try {
            userController.findUserById(1L);
        } catch (UserNotFoundException ex) {
            assertEquals("Usuario con el ID 1 no fue encontrado", ex.getMessage());
        }
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setName("Dan");

        when(userService.updateUser(1L, user)).thenReturn(user);

        ResponseEntity<User> response = userController.updateUser(1L, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void testUpdateUser_NotFound() {
        User user = new User();

        when(userService.updateUser(1L, user)).thenThrow(new UserNotFoundException("Usuario con el ID 1 no fue encontrado"));

        try {
            userController.updateUser(1L, user);
        } catch (UserNotFoundException ex) {
            assertEquals("Usuario con el ID 1 no fue encontrado", ex.getMessage());
        }
    }

    @Test
    public void testUpdateEmail() {
        User user = new User();
        user.setUserId(1L);
        user.setEmail("dani.seg@gmail.com");

        when(userService.updateEmail(1L, "dani.seg@gmail.com")).thenReturn(user);

        ResponseEntity<User> response = userController.updateEmail(1L, "dani.seg@gmail.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

        verify(userService).updateEmail(1L, "dani.seg@gmail.com");
    }

    @Test
    public void testUpdateEmailInvalidFormat() {
        String invalidEmail = "invalidCorreo.com";

        DataValidationException exception = assertThrows(DataValidationException.class, () -> {
            userController.updateEmail(1L, invalidEmail);
        });

        assertEquals("El formato del correo electrónico no es válido", exception.getMessage());
    }

    @Test
    public void testUpdateEmailNotFound() {
        when(userService.updateEmail(1L, "dani.seg@gmail.com")).thenThrow(new UserNotFoundException("Usuario con el ID 1 no fue encontrado"));

        try {
            userController.updateEmail(1L, "dani.seg@gmail.com");
        } catch (UserNotFoundException ex) {
            assertEquals("Usuario con el ID 1 no fue encontrado", ex.getMessage());
        }
    }

    @Test
    public void testDeleteUser() {
        when(userService.deleteUser(1L)).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteUser_NotFound() {
        when(userService.deleteUser(1L)).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSearchUsersValid() {
        User user = new User();
        user.setName("Daniel");
        user.setLastName("Gaspar");

        when(userService.searchUsers("Daniel", "Gaspar")).thenReturn(Collections.singletonList(user));

        ResponseEntity<List<User>> response = userController.searchUsers("Daniel", "Gaspar");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(user, response.getBody().get(0));

        verify(userService, times(1)).searchUsers("Daniel", "Gaspar");
    }

    @Test
    public void testSearchUsersInvalidName() {
        assertThrows(DataValidationException.class, () -> {
            userController.searchUsers("Daniel123", "Gaspar");
        });

        verify(userService, never()).searchUsers(anyString(), anyString());
    }

    @Test
    public void testSearchUsersInvalidLastName() {
        assertThrows(DataValidationException.class, () -> {
            userController.searchUsers("Daniel", "Gaspar123");
        });

        verify(userService, never()).searchUsers(anyString(), anyString());
    }

    @Test
    public void testSearchUsersNotFound() {
        when(userService.searchUsers("Gabriel", "Hernandez")).thenReturn(Collections.emptyList());

        ResponseEntity<List<User>> response = userController.searchUsers("Gabriel", "Hernandez");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());

        verify(userService, times(1)).searchUsers("Gabriel", "Hernandez");
    }

    @Test
    public void testFindUsersSortedByName() {
        User user = new User();
        user.setName("Daniel");

        when(userService.findUsersSortedByName()).thenReturn(Collections.singletonList(user));

        ResponseEntity<List<User>> response = userController.findUsersSortedByName();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(user, response.getBody().get(0));

        verify(userService, times(1)).findUsersSortedByName();
    }

    @Test
    public void testFindUsersSortedByNameNoContent() {
        when(userService.findUsersSortedByName()).thenReturn(Collections.emptyList());

        ResponseEntity<List<User>> response = userController.findUsersSortedByName();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(null, response.getBody());

        verify(userService, times(1)).findUsersSortedByName();
    }

    @Test
    public void testFindUsersOver18() {
        User user = new User();
        user.setName("Daniel");

        when(userService.findUsersOver18()).thenReturn(Collections.singletonList(user));

        ResponseEntity<List<User>> response = userController.findUsersOver18();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(user, response.getBody().get(0));

        verify(userService, times(1)).findUsersOver18();
    }

    @Test
    public void testFindUsersOver18NoContent() {
        when(userService.findUsersOver18()).thenReturn(Collections.emptyList());

        ResponseEntity<List<User>> response = userController.findUsersOver18();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(null, response.getBody());

        verify(userService, times(1)).findUsersOver18();
    }
}
