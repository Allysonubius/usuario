package com.backend.usuario.util;

import com.backend.usuario.exception.UserServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@SpringBootTest(classes = EmailValidatorUtil.class)
class EmailValidatorUtilTest {

    @Test
    void testValidate_ValidEmail() {
        EmailValidatorUtil util = new EmailValidatorUtil();
        String validEmail = "test@example.com";

        boolean isValid = util.validate(validEmail);

        assertTrue(isValid);
    }

    @Test
    void testConstructorWithNullMessage() {
        EmailValidatorUtil util = new EmailValidatorUtil();
        String validEmail = "test.email";

        boolean isValid = util.validate(validEmail);

        assertFalse(isValid);
    }
}