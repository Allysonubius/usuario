package com.backend.usuario.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserEntity.class)
class UserEntityTest {

    @InjectMocks
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = mock(UserEntity.class);
    }

    @Test
    void testGettersAndSetters() {
        UUID id = UUID.randomUUID();
        String username = "testuser";
        String password = "testpassword";
        Date dateCreate = new Date();
        Date dateUpdate = new Date();
        String email = "test@example.com";
        UserRoleEntity role = new UserRoleEntity(1L, "ADMIN");
        String active = "true";

        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setDateCreate(dateCreate);
        userEntity.setDateUpdate(dateUpdate);
        userEntity.setEmail(email);
        userEntity.setRole(role);
        userEntity.setActive(active);

        assertEquals(id, userEntity.getId());
        assertEquals(username, userEntity.getUsername());
        assertEquals(password, userEntity.getPassword());
        assertEquals(dateCreate, userEntity.getDateCreate());
        assertEquals(dateUpdate, userEntity.getDateUpdate());
        assertEquals(email, userEntity.getEmail());
        assertEquals(role, userEntity.getRole());
        assertEquals(active, userEntity.getActive());
    }

    @Test
    void testHashCode() {
        UUID id = UUID.randomUUID();
        UserEntity userEntity1 = new UserEntity(id, "testuser", "testpassword", new Date(), new Date(), "test@example.com", new UserRoleEntity(1L, "ADMIN"), "true");
        UserEntity userEntity2 = new UserEntity(id, "testuser", "testpassword", new Date(), new Date(), "test@example.com", new UserRoleEntity(1L, "ADMIN"), "true");
        UserEntity userEntity3 = new UserEntity(UUID.randomUUID(), "testuser", "testpassword", new Date(), new Date(), "test@example.com", new UserRoleEntity(1L, "ADMIN"), "true");

        assertEquals(userEntity1.hashCode(), userEntity2.hashCode());
        assertNotEquals(userEntity1.hashCode(), userEntity3.hashCode());
    }

    @Test
    void testToString() {
        UUID id = UUID.randomUUID();
        UserEntity userEntity = new UserEntity(id, "testuser", "testpassword", new Date(), new Date(), "test@example.com", new UserRoleEntity(1L, "ADMIN"), "true");
        String expectedString = "UserEntity(id=" + id + ", username=testuser, password=testpassword, dateCreate=" + userEntity.getDateCreate() + ", dateUpdate=" + userEntity.getDateUpdate() + ", email=test@example.com, role=UserRoleEntity(id=1, role=ADMIN), active=true)";
        assertEquals(expectedString, userEntity.toString());
    }

}