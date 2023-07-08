package com.backend.usuario.service;

import com.backend.usuario.config.data.jwt.JwtUtils;
import com.backend.usuario.domain.request.user.UserLoginRequest;
import com.backend.usuario.domain.response.erro.ErrorResponse;
import com.backend.usuario.domain.response.jwt.JwtResponse;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = LoginService.class)
class LoginServiceTest {

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private LoginService loginService;

    @BeforeEach
    void setup(){
        loginService = new LoginService(
                userRepository,
                jwtUtils,
                authenticationManager);
    }

    @Test
    void testLoginUser_Success() {
        // Arrange
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUsername("testuser");
        userLoginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        JwtUtils jwtUtils = mock(JwtUtils.class);
        when(jwtUtils.generateJwtToken(any(Authentication.class), any(UserLoginRequest.class))).thenReturn("jwt_token");

        // Act
        ResponseEntity<Object> response = this.loginService.loginUser(userLoginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof JwtResponse);
    }

    @Test
    void testLoginUser_UserServiceException() {
        UserLoginRequest user = new UserLoginRequest();
        user.setUsername("username");
        user.setPassword("password");

        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mockito.mock(Authentication.class));
        when(jwtUtils.generateJwtToken(Mockito.any(Authentication.class), Mockito.any(UserLoginRequest.class))).thenThrow(UserServiceException.class);

        // Act and Assert
        ResponseEntity<Object> response = this.loginService.loginUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponse);

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("Error during user login.", errorResponse.getMessage());

        verify(authenticationManager, times(1)).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateJwtToken(Mockito.any(Authentication.class), Mockito.any(UserLoginRequest.class));
    }

    @Test
    void testRefresh_success() {
        String username = "testuser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(jwtUtils.createToken(username, Optional.of(userEntity))).thenReturn("testtoken");

        String token = this.loginService.refresh(username);

        assertNotNull(token);
        assertEquals("testtoken", token);
        verify(userRepository, times(1)).findByUsername(username);
        verify(jwtUtils, times(1)).createToken(username, Optional.of(userEntity));
    }
    @Test
    void testRefresh_failure() {
        String username = "";

        assertThatThrownBy(() ->
                this.loginService.refresh(username))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Username is empty");

        verify(userRepository, never()).findByUsername(anyString());
        verify(jwtUtils, never()).createToken(anyString(), any());
    }
}