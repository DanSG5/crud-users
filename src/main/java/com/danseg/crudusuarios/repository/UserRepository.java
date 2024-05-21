package com.danseg.crudusuarios.repository;

import com.danseg.crudusuarios.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    // Método que busca el email del usuario en la BD para autentificarlo
    User findByEmail(String email);

    // Métodos adicionales para búsqueda y filtrado de usuarios
    // Métodos para encontrar usuarios por nombre o apellido
    List<User> findByNameAndLastName(String name, String lastName);
    List<User> findByName(String name);
    List<User> findByLastName(String lastName);

    // Método para obtener a los usuarios ordenados alfabéticamente por su nombre
    List<User> findAllByOrderByNameAsc();

    // Método para obtener a los usuarios mayores de 18 años
    List<User> findByBirthdayBefore(LocalDate cutoffDate);
}
