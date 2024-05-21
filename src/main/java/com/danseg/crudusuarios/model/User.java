package com.danseg.crudusuarios.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 30, message = "El nombre debe tener entre 3 y 30 caracteres")
    @Pattern(regexp = "[A-Za-z ]+", message = "El nombre sólo puede contener letras")
    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 4, max = 30, message = "El nombre debe tener entre 4 y 30 caracteres")
    @Pattern(regexp = "[A-Za-z ]+", message = "El nombre sólo puede contener letras")
    @Column(name = "last_name", length = 30, nullable = false)
    private String lastName;

    @Pattern(regexp = "\\d{0,10}", message = "El teléfono sólo puede contener 0 y 10 dígitos")
    @Column(name = "phone_number", length = 10, nullable = true)
    private String phoneNumber;

    @Email(regexp = "[A-za-z0-9._%+-]+@[a-z0-9.-]+\\.[A-Za-z]{2,3}", message = "El email debe ser válido")
    @Size(max = 40, message = "El email no puede tener más de 40 caracteres")
    @Column(name = "email", length = 40, nullable = false)
    private String email;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 1, max = 160, message = "La contraseña debe contener entre 8 y 20 carácteres")
    @Column(name = "password", length = 160, nullable = false)
    private String password;

    public User() {
    }

    public User(String name, String lastName, String phoneNumber, String email) {
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", password=" + password +
                '}';
    }
}
