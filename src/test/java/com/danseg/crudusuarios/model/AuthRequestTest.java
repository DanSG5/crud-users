package com.danseg.crudusuarios.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthRequestTest {

    @Test
    public void AuthRequestTest() {
        String email = "ms2@hotmail.com";
        String password = "admin546";
        AuthRequest ar = new AuthRequest(email, password);
        assertEquals(email, ar.getEmail());
        assertEquals(password, ar.getPassword());
    }

    @Test
    public void testSetEmail() {
        AuthRequest ar = new AuthRequest();
        ar.setEmail("leon@zoo.com");
        assertEquals("leon@zoo.com", ar.getEmail());
    }

    @Test
    public void testSetPassword() {
        AuthRequest ar = new AuthRequest();
        ar.setPassword("password123");
        assertEquals("password123", ar.getPassword());
    }

    @Test
    public void toStringTest() {
        String email = "pe.la@google.mx";
        String password = "estilo/123";

        AuthRequest ar = new AuthRequest();
        ar.setEmail(email);
        ar.setPassword(password);

        String expectedToString = "AuthRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';

        String actualToString = ar.toString();

        assertEquals(expectedToString, actualToString);
    }
}
