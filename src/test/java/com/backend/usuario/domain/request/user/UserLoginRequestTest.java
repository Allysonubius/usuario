package com.backend.usuario.domain.request.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UserLoginRequest.class)
class UserLoginRequestTest {

    @Test
    void testGettersAndSetters() {
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("testuser");
        userLoginRequest.setPassword("testpassword");

        assertEquals("testuser", userLoginRequest.getUsername());
        assertEquals("testpassword", userLoginRequest.getPassword());
    }

    @Test
    void testEquals() {
        UserLoginRequest userLoginRequest1 = new UserLoginRequest("testuser", "testpassword");
        UserLoginRequest userLoginRequest2 = new UserLoginRequest("testuser", "testpassword");
        UserLoginRequest userLoginRequest3 = new UserLoginRequest("testuser", "differentpassword");

        assertTrue(userLoginRequest1.equals(userLoginRequest2));
        assertFalse(userLoginRequest1.equals(userLoginRequest3));
    }

    @Test
    void testHashCode() {
        UserLoginRequest userLoginRequest1 = new UserLoginRequest("testuser", "testpassword");
        UserLoginRequest userLoginRequest2 = new UserLoginRequest("testuser", "testpassword");
        UserLoginRequest userLoginRequest3 = new UserLoginRequest("testuser", "differentpassword");

        assertEquals(userLoginRequest1.hashCode(), userLoginRequest2.hashCode());
        assertNotEquals(userLoginRequest1.hashCode(), userLoginRequest3.hashCode());
    }

    @Test
    void testToString() {
        UserLoginRequest userLoginRequest = new UserLoginRequest("testuser", "testpassword");
        String expectedString = "UserLoginRequest(username=testuser, password=testpassword)";
        assertEquals(expectedString, userLoginRequest.toString());
    }
}