package com.danseg.crudusuarios.service.impl;

import com.danseg.crudusuarios.exception.UserNotFoundException;
import com.danseg.crudusuarios.model.User;
import com.danseg.crudusuarios.repository.UserRepository;
import com.danseg.crudusuarios.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    // Variable para usar la dependencia
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    // Inyección de la dependencia
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método para guardar un usuario usando el método save predeterminado de JpaRepository
    @Override
    public User saveUser(User user) {
        user.setName(user.getName().toUpperCase());
        user.setLastName(user.getLastName().toUpperCase());
        user.setEmail(user.getEmail().toLowerCase());
        // Cifra la contraseña antes de guardarla
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }

    // Método para guardar un usuario en la BD
    @Override
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    // Método para encontrar un usuario en la BD por su ID
    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("El usuario con ID: " + userId + " no existe"));
    }

    // Método para actualizar la información de un usuario en la BD por su ID
    @Override
    public User updateUser(Long userId, User user) {
        User newData = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("El usuario con ID: " + userId + " no existe"));
        newData.setName(user.getName().toUpperCase());
        newData.setLastName(user.getLastName().toUpperCase());
        newData.setPhoneNumber(user.getPhoneNumber());
        newData.setEmail(user.getEmail().toLowerCase());
        newData.setBirthday(user.getBirthday());
        // Cifra la contraseña antes de guardarla
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        newData.setPassword(encryptedPassword);

        return userRepository.save(newData);
    }

    // Método para actualizar el email de un usuario en la BD por su ID
    @Override
    public User updateEmail(Long userId, String email) {
        User newEmail = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("El usuario con ID: " + userId + " no existe"));
        newEmail.setEmail(email.toLowerCase());

        return userRepository.save(newEmail);
    }

    // Método que elimina un usuario de la BD por su ID
    @Override
    public Boolean deleteUser(Long userId) {
        boolean isInDB = userRepository.existsById(userId);
        if (!isInDB) {
            return false;
        }
        userRepository.deleteById(userId);
        return true;
    }

    // Métodos adicionales para búsqueda y filtrado de usuarios
    // Método para encontrar usuarios por nombre o apellido
    @Override
    public List<User> searchUsers(String name, String lastName) {
        if (name != null && lastName != null) {
            return userRepository.findByNameAndLastName(name, lastName);
        } else if (name != null) {
            return userRepository.findByName(name);
        } else if (lastName != null) {
            return userRepository.findByLastName(lastName);
        } else {
            return userRepository.findAll();
        }
    }

    // Método para obtener a los usuarios ordenados alfabéticamente por su nombre
    @Override
    public List<User> findUsersSortedByName() {
        return userRepository.findAllByOrderByNameAsc();
    }

    // Método para obtener a los usuarios mayores de 18 años
    @Override
    public List<User> findUsersOver18() {
        LocalDate cutoffDate = LocalDate.now().minusYears(18);
        return userRepository.findByBirthdayBefore(cutoffDate);
    }
}
