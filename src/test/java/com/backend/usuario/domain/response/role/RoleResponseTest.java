package com.backend.usuario.domain.response.role;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RoleResponse.class)
class RoleResponseTest {

    @Test
    void testGettersAndSetters() {
        String id = "123";
        String role = "ADMIN";

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(id);
        roleResponse.setRole(role);

        assertEquals(id, roleResponse.getId());
        assertEquals(role, roleResponse.getRole());
    }

    @Test
    void testEquals() {
        RoleResponse roleResponse1 = new RoleResponse();
        roleResponse1.setId(String.valueOf(1));
        roleResponse1.setRole("ADMIN");

        assertTrue(roleResponse1.equals(roleResponse1));
    }

    @Test
    void testHashCode() {
        RoleResponse roleResponse1 = new RoleResponse();
        roleResponse1.setId(String.valueOf(1));
        roleResponse1.setRole("ADMIN");

        assertEquals(roleResponse1.hashCode(), roleResponse1.hashCode());
    }

    @Test
    void testToString() {
        RoleResponse roleResponse1 = new RoleResponse();
        roleResponse1.setId(String.valueOf(1));
        roleResponse1.setRole("ADMIN");
        String expectedString = "RoleResponse(id=1, role=ADMIN)";
        assertEquals(expectedString, roleResponse1.toString());
    }
}