package com.backend.usuario.util;

import com.backend.usuario.exception.UserServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = EmailValidatorUtil.class)
class EmailValidatorUtilTest {

    @Test
    public void testConstructorWithMessage() {
        String errorMessage = "Error occurred in the user service.";
        UserServiceException exception = new UserServiceException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testConstructorWithNullMessage() {
        UserServiceException exception = new UserServiceException(null);
        assertEquals(null, exception.getMessage());
    }
}