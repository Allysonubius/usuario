package com.backend.usuario.domain.request.role;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = RoleUserRequest.class)
class RoleUserRequestTest {

    @Test
    void testGettersAndSetters() {
        Long id = 123L;

        RoleUserRequest roleUserRequest = new RoleUserRequest();
        roleUserRequest.setId(id);

        assertEquals(id, roleUserRequest.getId());
    }

    @Test
    void testValidation() {
        RoleUserRequest roleUserRequest = new RoleUserRequest();

        String validId = "12345";
        roleUserRequest.setId(Long.parseLong(validId));
        assertEquals(Long.valueOf(validId), roleUserRequest.getId());


    }
}