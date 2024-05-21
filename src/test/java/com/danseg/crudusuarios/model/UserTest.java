package com.danseg.crudusuarios.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    @Test
    public void UserTest() {
        String name = "Juan";
        String lastName = "Segovia";
        String phoneNumber = "5534567890";
        String email = "juan.seg@gmail.com";
        User user = new User(name, lastName, phoneNumber, email);
        assertEquals(name, user.getName());
        assertEquals(lastName, user.getLastName());
        assertEquals(phoneNumber, user.getPhoneNumber());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testSetUserId() {
        User user = new User();
        user.setUserId(25L);
        assertEquals(25L, user.getUserId());
    }

    @Test
    public void testSetName() {
        User user = new User();
        user.setName("John");
        assertEquals("John", user.getName());
    }

    @Test
    public void testSetLastName() {
        User user = new User();
        user.setLastName("Pérez");
        assertEquals("Pérez", user.getLastName());
    }

    @Test
    public void testSetEmail() {
        User user = new User();
        user.setEmail("delfin@acuario.com");
        assertEquals("delfin@acuario.com", user.getEmail());
    }

    @Test
    public void testSetPassword() {
        User user = new User();
        user.setPassword("secreto123");
        assertEquals("secreto123", user.getPassword());
    }

    @Test
    public void testSetPhoneNumber() {
        User user = new User();
        user.setPhoneNumber("5512654898");
        assertEquals("5512654898", user.getPhoneNumber());
    }

    @Test
    public void testSetBirthday() {
        User user = new User();
        user.setBirthday(LocalDate.of(1998,05,15));
        assertEquals(LocalDate.of(1998,05,15), user.getBirthday());
    }

    @Test
    public void toStringTest() {
        Long userId = 2L;
        String name = "Julio";
        String lastName = "Gutierrez";
        String phoneNumber = "5534567890";
        String email = "jul.gu@empresa.com";
        LocalDate birthday = LocalDate.of(1970, 11, 26);
        String password = "password123";

        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        user.setBirthday(birthday);
        user.setPassword(password);

        String expectedToString = "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", password=" + password +
                '}';

        String actualToString = user.toString();

        assertEquals(expectedToString, actualToString);
    }
}

