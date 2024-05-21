package com.danseg.crudusuarios.service.impl;

import com.danseg.crudusuarios.exception.UserNotFoundException;
import com.danseg.crudusuarios.model.User;
import com.danseg.crudusuarios.repository.UserRepository;
import com.danseg.crudusuarios.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUpTest () {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId(1L);
        user.setName("Dan");
        user.setLastName("Segovia");
        user.setEmail("dan.sego@gmail.com");
        user.setPhoneNumber("553456789");
        user.setPassword("password");
        user.setBirthday(LocalDate.of(1997, 8, 15));
    }

    @Test
    public void SaveUserTest () {
        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals("DAN", savedUser.getName());
        assertEquals("SEGOVIA", savedUser.getLastName());
        assertEquals("dan.sego@gmail.com", savedUser.getEmail());
        assertEquals("encryptedPassword", savedUser.getPassword());

        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void FindUsersTest () {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> users = userService.findUsers();

        assertNotNull(users);
        assertEquals(1, users.size());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void FindUserByIdTest () {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User foundUser = userService.findUserById(1L);

        assertNotNull(foundUser);
        assertEquals(user.getName(), foundUser.getName());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void FindUserByIdNotFoundTest () {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.findUserById(1L);
        });

        assertEquals("El usuario con ID: 1 no existe", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void UpdateUserTest () {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUser(1L, user);

        assertNotNull(updatedUser);
        assertEquals(user.getName(), updatedUser.getName());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void UpdateEmailTest () {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateEmail(1L, "new.email@outlook.com");

        assertNotNull(updatedUser);
        assertEquals("new.email@outlook.com", updatedUser.getEmail());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void DeleteUserTest () {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        Boolean isDeleted = userService.deleteUser(1L);

        assertTrue(isDeleted);

        verify(userRepository, times(1)).existsById(1L);
    }

    @Test
    public void DeleteUserNotFoundTest () {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        Boolean isDeleted = userService.deleteUser(1L);

        assertFalse(isDeleted);

        verify(userRepository, times(1)).existsById(1L);
    }

    @Test
    public void SearchUsersByNameAndLastNameTest () {
        when(userRepository.findByNameAndLastName(anyString(), anyString())).thenReturn(Collections.singletonList(user));

        List<User> users = userService.searchUsers("John", "Doe");

        assertNotNull(users);
        assertEquals(1, users.size());

        verify(userRepository, times(1)).findByNameAndLastName("John", "Doe");
    }

    @Test
    public void SearchUsersByNameTest () {
        when(userRepository.findByName(anyString())).thenReturn(Collections.singletonList(user));

        List<User> users = userService.searchUsers("John", null);

        assertNotNull(users);
        assertEquals(1, users.size());

        verify(userRepository, times(1)).findByName("John");
    }

    @Test
    public void SearchUsersByLastNameTest () {
        when(userRepository.findByLastName(anyString())).thenReturn(Collections.singletonList(user));

        List<User> users = userService.searchUsers(null, "Doe");

        assertNotNull(users);
        assertEquals(1, users.size());

        verify(userRepository, times(1)).findByLastName("Doe");
    }

    @Test
    public void SearchUsersNoCriteriaTest () {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> users = userService.searchUsers(null, null);

        assertNotNull(users);
        assertEquals(1, users.size());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void FindUsersSortedByNameTest () {
        when(userRepository.findAllByOrderByNameAsc()).thenReturn(Collections.singletonList(user));

        List<User> users = userService.findUsersSortedByName();

        assertNotNull(users);
        assertEquals(1, users.size());

        verify(userRepository, times(1)).findAllByOrderByNameAsc();
    }

    @Test
    public void FindUsersOver18Test () {
        LocalDate cutoffDate = LocalDate.now().minusYears(18);
        when(userRepository.findByBirthdayBefore(any(LocalDate.class))).thenReturn(Collections.singletonList(user));

        List<User> users = userService.findUsersOver18();

        assertNotNull(users);
        assertEquals(1, users.size());

        verify(userRepository, times(1)).findByBirthdayBefore(cutoffDate);
    }
}
