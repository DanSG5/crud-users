package com.danseg.crudusuarios.controller;

import com.danseg.crudusuarios.exception.DataValidationException;
import com.danseg.crudusuarios.exception.UserNotFoundException;
import com.danseg.crudusuarios.model.User;
import com.danseg.crudusuarios.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    // Variable para usar la dependencia
    private final UserService userService;

    // Inyección de la dependencia
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Método POST para guardar un usuario
    //Al usar ResponseEntity<> tenemos un mejor control sobre la respuesta que se le regresa al cliente
    @PostMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Método GET para obtener a todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> findUsers() {
        List<User> users = userService.findUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Método GET para obtener un usuario por su ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable Long userId) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("Usuario con el ID " + userId + " no fue encontrado");
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Método PUT para actualizar la información de un usuario por su ID
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        if (updatedUser == null) {
            throw new UserNotFoundException("Usuario con el ID " + userId + " no fue encontrado");
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // Método PATCH para actualizar el campo email de un usuario por su ID
    @PatchMapping("/{userId}")
    public ResponseEntity<User> updateEmail(@PathVariable Long userId, @RequestBody String email) {
        boolean isValid = email.matches("[A-Za-z0-9._%+-]+@[a-z0-9.-]+\\.[A-Za-z]{2,3}");
        if (!isValid) {
            throw new DataValidationException("El formato del correo electrónico no es válido");
        }
        User updatedUser = userService.updateEmail(userId, email);
        if (updatedUser == null) {
            throw new UserNotFoundException("Usuario con el ID " + userId + " no fue encontrado");
        }
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // Método DELETE para eliminar a un usuario por su ID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        boolean isDeleted = userService.deleteUser(userId);
        if (!isDeleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Métodos adicionales para búsqueda y filtrado de usuarios
    // Método avanzado para filtrar usuarios por nombre o correo electrónico
    @GetMapping("/search/by")
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String lastName) {
        if (name != null || lastName != null) {
            if (name != null) {
                boolean isValidName = name.matches("[A-za-z]+");
                if (!isValidName) {
                    throw new DataValidationException("El nombre ingresado no es válido");
                }
            }
            if (lastName != null) {
                boolean isValidLastName = lastName.matches("[A-za-z]+");
                if (!isValidLastName) {
                    throw new DataValidationException("El apellido ingresado no es válido");
                }
            }
        }
        List<User> users = userService.searchUsers(name, lastName);
        if (!users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Método GET para obtener a los usuarios ordenados alfabéticamente por su nombre
    @GetMapping("/ordered")
    public ResponseEntity<List<User>> findUsersSortedByName() {
        List<User> users = userService.findUsersSortedByName();
        if (users.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Método GET para obtener a los usuarios mayores de 18 años
    @GetMapping("/over/eighteen")
    public ResponseEntity<List<User>> findUsersOver18() {
        List<User> users = userService.findUsersOver18();
        if (users.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
