package com.backend.usuario.constants;

import com.backend.usuario.UserApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = ErrorMessage.class)
class ErrorMessageTest {

    @Test
    void getValue() {
        String expectedValue = "Input data is missing or is incorrect";
        ErrorMessage errorMessage = ErrorMessage.WRONGDATA;

        String actualValue = errorMessage.getValue();

        assertEquals(expectedValue, actualValue);
    }
}