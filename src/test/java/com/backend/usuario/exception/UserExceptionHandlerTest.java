package com.backend.usuario.exception;

import com.backend.usuario.UserApplication;
import com.backend.usuario.domain.response.erro.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserExceptionHandler.class)
class UserExceptionHandlerTest {

    @Test
    void handleAccessDeniedException() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        UserExceptionHandler exceptionHandler = new UserExceptionHandler();

        exceptionHandler.handleAccessDeniedException(response);

        verify(response, times(1)).sendError(eq(HttpStatus.FORBIDDEN.value()), eq("Access denied"));

    }

    @Test
    void handleInternalServerError() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Exception exception = new Exception("Internal Server Error");
        UserExceptionHandler exceptionHandler = new UserExceptionHandler();

        when(request.getRequestURI()).thenReturn("/users");

        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleInternalServerError(request, exception);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "/users",
                LocalDateTime.now());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void handleValidationExceptions() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                null, mock(BindingResult.class));
        UserExceptionHandler exceptionHandler = new UserExceptionHandler();

        when(request.getRequestURI()).thenReturn("/users");

        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("user", "name", "Name is required"));
        fieldErrors.add(new FieldError("user", "email", "Invalid email format"));
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleValidationExceptions(request, exception);

        ErrorResponse expectedErrorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Name is required, Invalid email format",
                "/users",
                LocalDateTime.now());

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}