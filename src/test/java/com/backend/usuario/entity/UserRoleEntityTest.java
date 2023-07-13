package com.backend.usuario.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = UserRoleEntity.class)
class UserRoleEntityTest {

    @InjectMocks
    private UserRoleEntity userRoleEntity;

    @BeforeEach
    void setup(){
        userRoleEntity = mock(UserRoleEntity.class);
    }

    @Test
    void testGettersAndSetters() {
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setId(1L);
        userRoleEntity.setRole("ADMIN");

        assertEquals("ADMIN", userRoleEntity.getRole());
    }

    @Test
    void testEquals() {
        UserRoleEntity userRoleEntity1 = new UserRoleEntity(1L, "ADMIN");
        UserRoleEntity userRoleEntity2 = new UserRoleEntity(1L, "ADMIN");
        UserRoleEntity userRoleEntity3 = new UserRoleEntity(2L, "ADMIN");

        assertTrue(userRoleEntity1.equals(userRoleEntity2));
        assertFalse(userRoleEntity1.equals(userRoleEntity3));
    }

    @Test
    void testHashCode() {
        UserRoleEntity userRoleEntity1 = new UserRoleEntity(1L, "ADMIN");
        UserRoleEntity userRoleEntity2 = new UserRoleEntity(1L, "ADMIN");
        UserRoleEntity userRoleEntity3 = new UserRoleEntity(2L, "ADMIN");

        assertEquals(userRoleEntity1.hashCode(), userRoleEntity2.hashCode());
        assertNotEquals(userRoleEntity1.hashCode(), userRoleEntity3.hashCode());
    }

    @Test
    void testToString() {
        UserRoleEntity userRoleEntity = new UserRoleEntity(1L, "ADMIN");
        String expectedString = "UserRoleEntity(id=1, role=ADMIN)";
        assertEquals(expectedString, userRoleEntity.toString());
    }
}