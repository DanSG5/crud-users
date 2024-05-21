package com.danseg.crudusuarios.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthResponseTest {

    @Test
    public void AuthResponseTest() {
        String jwt = "askjna54sd2s4das6658a46s5d13a*/asd.";
        AuthResponse ar = new AuthResponse(jwt);
        assertEquals(jwt, ar.getJwt());
    }

    @Test
    public void toStringTest() {
        String jwt = "askjna54sd2s4das6658a46s5d13a*/asd.";
        AuthResponse authResponse = new AuthResponse(jwt);

        String expectedToString = "AuthResponse{" +
                "jwt='" + jwt + '\'' +
                '}';
        String actualToString = authResponse.toString();

        assertEquals(expectedToString, actualToString);
    }
}
