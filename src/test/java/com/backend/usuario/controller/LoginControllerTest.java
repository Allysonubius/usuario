package com.backend.usuario.controller;

import com.backend.usuario.domain.request.user.UserLoginRequest;
import com.backend.usuario.domain.response.erro.ErrorResponse;
import com.backend.usuario.domain.response.jwt.JwtResponse;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes =  LoginController.class)
class LoginControllerTest {

    @MockBean
    private LoginController loginController;

    @MockBean
    private LoginService loginService;

    @BeforeEach
    void setup(){
        this.loginController = new LoginController(
                loginService
        );
    }

    @Test
    void testLoginUser_Success() {
        UserLoginRequest loginRequest = new UserLoginRequest("username", "password");

        ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.OK).body("Usuario logado com sucesso !");

        when(loginService.loginUser(loginRequest)).thenReturn(responseEntity);

        ResponseEntity<Object> response = this.loginController.loginUser(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario logado com sucesso !", response.getBody());
    }

    @Test
    void testRefreshUser_Success() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRemoteUser()).thenReturn("username");

        String token = "new_token";

        ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(token));

        when(loginService.refresh("username")).thenReturn(token);

        ResponseEntity<Object> response = this.loginController.refreshUser(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new JwtResponse(token), response.getBody());
    }
    @Test
    void testRefreshUser_Exception() {
        // Creating a mock HttpServletRequest
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRemoteUser()).thenReturn("username");

        // Creating a mock exception
        UserServiceException exception = new UserServiceException("Error refreshing token");

        // Mocking the LoginService to throw an exception
        when(loginService.refresh("username")).thenThrow(exception);

        // Calling the API endpoint
        ResponseEntity<Object> response = this.loginController.refreshUser(request);

        // Verifying the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(HttpStatus.BAD_GATEWAY.value(), errorResponse.getStatus());
        assertEquals("Error refreshing token", errorResponse.getMessage());
        assertEquals("/api/refresh", errorResponse.getPath());
    }
}