package com.backend.usuario.domain.request.user;

import com.backend.usuario.domain.request.role.RoleUserRequest;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserCreateUserRequestTest {
    @Test
    void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        String username = "testuser";
        String password = "testpassword";
        String dateCreate = "2023-07-12 12:00:00";
        String dateUpdate = "2023-07-12 12:00:00";
        String email = "test@example.com";
        RoleUserRequest role = new RoleUserRequest();
        role.setId(1L);
        String active = "true";

        UserCreateUserRequest userCreateUserRequest = new UserCreateUserRequest();
        userCreateUserRequest.setId(id);
        userCreateUserRequest.setUsername(username);
        userCreateUserRequest.setPassword(password);
        userCreateUserRequest.setDateCreate(dateCreate);
        userCreateUserRequest.setDateUpdate(dateUpdate);
        userCreateUserRequest.setEmail(email);
        userCreateUserRequest.setRole(role);
        userCreateUserRequest.setActive(active);

        assertEquals(id, userCreateUserRequest.getId());
        assertEquals(username, userCreateUserRequest.getUsername());
        assertEquals(password, userCreateUserRequest.getPassword());
        assertEquals(dateCreate, userCreateUserRequest.getDateCreate());
        assertEquals(dateUpdate, userCreateUserRequest.getDateUpdate());
        assertEquals(email, userCreateUserRequest.getEmail());
        assertEquals(role, userCreateUserRequest.getRole());
        assertEquals(active, userCreateUserRequest.getActive());
    }

    @Test
    void testEquals() {
        UUID id = UUID.randomUUID();
        UserCreateUserRequest userCreateUserRequest1 = new UserCreateUserRequest(id, "testuser", "testpassword", "2023-07-12 12:00:00", "2023-07-12 12:00:00", "test@example.com", new RoleUserRequest(), "true");
        UserCreateUserRequest userCreateUserRequest2 = new UserCreateUserRequest(id, "testuser", "testpassword", "2023-07-12 12:00:00", "2023-07-12 12:00:00", "test@example.com", new RoleUserRequest(), "true");
        UserCreateUserRequest userCreateUserRequest3 = new UserCreateUserRequest(UUID.randomUUID(), "testuser", "testpassword", "2023-07-12 12:00:00", "2023-07-12 12:00:00", "test@example.com", new RoleUserRequest(), "true");

        assertTrue(userCreateUserRequest1.equals(userCreateUserRequest2));
        assertFalse(userCreateUserRequest1.equals(userCreateUserRequest3));
    }

    @Test
    void testHashCode() {
        UUID id = UUID.randomUUID();
        UserCreateUserRequest userCreateUserRequest1 = new UserCreateUserRequest(id, "testuser", "testpassword", "2023-07-12 12:00:00", "2023-07-12 12:00:00", "test@example.com", new RoleUserRequest(), "true");
        UserCreateUserRequest userCreateUserRequest2 = new UserCreateUserRequest(id, "testuser", "testpassword", "2023-07-12 12:00:00", "2023-07-12 12:00:00", "test@example.com", new RoleUserRequest(), "true");
        UserCreateUserRequest userCreateUserRequest3 = new UserCreateUserRequest(UUID.randomUUID(), "testuser", "testpassword", "2023-07-12 12:00:00", "2023-07-12 12:00:00", "test@example.com", new RoleUserRequest(), "true");

        assertEquals(userCreateUserRequest1.hashCode(), userCreateUserRequest2.hashCode());
        assertNotEquals(userCreateUserRequest1.hashCode(), userCreateUserRequest3.hashCode());
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        UserCreateUserRequest userCreateUserRequest = new UserCreateUserRequest(id, "testuser", "testpassword", "2023-07-12 12:00:00", "2023-07-12 12:00:00", "test@example.com", new RoleUserRequest(1L), "true");
        String expectedString = "UserCreateUserRequest(id=" + id + ", username=testuser, password=testpassword, dateCreate=2023-07-12 12:00:00, dateUpdate=2023-07-12 12:00:00, email=test@example.com, role=RoleUserRequest(id=1), active=true)";
        assertEquals(expectedString, userCreateUserRequest.toString());
    }

}