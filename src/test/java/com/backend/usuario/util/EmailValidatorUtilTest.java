package com.backend.usuario.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = EmailValidatorUtil.class)
class EmailValidatorUtilTest {

    @Test
    public void testValidate_ValidEmail_ReturnsTrue() {
        EmailValidatorUtil emailValidator = new EmailValidatorUtil();
        String validEmail = "test@example.com";
        assertTrue(emailValidator.validate(validEmail));
    }

    @Test
    public void testValidate_InvalidEmail_ReturnsFalse() {
        EmailValidatorUtil emailValidator = new EmailValidatorUtil();
        String invalidEmail = "invalidemail";
        assertFalse(emailValidator.validate(invalidEmail));
    }
}