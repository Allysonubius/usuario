package com.backend.usuario.domain.request.user;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = UserCreateUserRequest.class)
class UserRequestTest {

    @Test
    void testConstructorAndGetters() {
//        String username = "johndoe";
//        String password = "password123";
//        UserRequest userRequest = new UserRequest(username, password);
//
//        assertNull(userRequest.getId());
//        assertEquals(username, userRequest.getUsername());
//        assertEquals(password, userRequest.getPassword());
//        assertNull(userRequest.getDateCreate());
//        assertNull(userRequest.getDateUpdate());
    }

    @Test
    void testValidation() {
//        UserRequest userRequest = new UserRequest("", "");
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        Validator validator = factory.getValidator();
//        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
//
//        assertFalse(violations.isEmpty());
//        assertEquals(2, violations.size());
    }
}