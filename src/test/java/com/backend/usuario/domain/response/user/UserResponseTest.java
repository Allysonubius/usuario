package com.backend.usuario.domain.response.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = UserResponse.class)
class UserResponseTest {
    @Test
    void testConstructorAndGetters() {
        String id = "123";
        String username = "johndoe";
        String dateCreate = "2022-03-14T12:00:00Z";
        String dateUpdate = "2022-03-15T08:30:00Z";
        UserResponse userResponse = new UserResponse();
        userResponse.setId(id);
        userResponse.setUsername(username);
        userResponse.setDateCreate(dateCreate);
        userResponse.setDateUpdate(dateUpdate);

        assertEquals(id, userResponse.getId());
        assertEquals(username, userResponse.getUsername());
        assertEquals(dateCreate, userResponse.getDateCreate());
        assertEquals(dateUpdate, userResponse.getDateUpdate());
    }
    @Test
    void testSetterAndGetters() {
        String id = "123";
        String username = "johndoe";
        String dateCreate = "2022-03-14T12:00:00Z";
        String dateUpdate = "2022-03-15T08:30:00Z";
        UserResponse userResponse = new UserResponse();
        userResponse.setId(id);
        userResponse.setUsername(username);
        userResponse.setDateCreate(dateCreate);
        userResponse.setDateUpdate(dateUpdate);

        assertEquals(id, userResponse.getId());
        assertEquals(username, userResponse.getUsername());
        assertEquals(dateCreate, userResponse.getDateCreate());
        assertEquals(dateUpdate, userResponse.getDateUpdate());
    }
}