package com.backend.usuario.domain.response.jwt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {
    @Test
    public void testConstructorAndGetters() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        JwtResponse jwtResponse = new JwtResponse(token);

        assertEquals(token, jwtResponse.getToken());
    }
    @Test
    public void testSetterAndGetters() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setToken(token);

        assertEquals(token, jwtResponse.getToken());
    }
}