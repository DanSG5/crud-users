package com.danseg.crudusuarios.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
    }

    @Test
    public void testExtractUsername() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtTokenUtil.generateToken(userDetails);
        String username = jwtTokenUtil.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    public void testExtractExpiration() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtTokenUtil.generateToken(userDetails);
        Date expiration = jwtTokenUtil.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtTokenUtil.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    public void testValidateToken() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtTokenUtil.generateToken(userDetails);
        Boolean isValid = jwtTokenUtil.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    public void testExtractClaim() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtTokenUtil.generateToken(userDetails);
        String username = jwtTokenUtil.extractClaim(token, Claims::getSubject);

        assertEquals("testuser", username);
    }
}

