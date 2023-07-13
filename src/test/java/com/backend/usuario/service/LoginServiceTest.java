package com.backend.usuario.service;

import com.backend.usuario.config.data.jwt.JwtUtils;
import com.backend.usuario.domain.request.user.UserLoginRequest;
import com.backend.usuario.domain.response.erro.ErrorResponse;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
    void testLoginUser() {
        // Criar um usuário de teste
        UserLoginRequest user = new UserLoginRequest();
        user.setUsername("testuser");
        user.setPassword("testpassword");

        // Criar um usuário de teste no banco de dados
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testuser");
        userEntity.setPassword("testpassword");
        userEntity.setActive("true");

        // Mock do UserRepository para retornar o usuário de teste
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));

        // Mock da autenticação bem-sucedida
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("testuser");
        when(authentication.isAuthenticated()).thenReturn(true);

        // Mock do contexto de segurança
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Chamar o método loginUser
        ResponseEntity<Object> response = this.loginService.loginUser(user);

        // Verificar se a resposta é OK (HttpStatus 200)
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verificar se o token JWT foi gerado
        assertNotNull(response.getBody());

        // Verificar se o método de autenticação foi chamado
        verify(authenticationManager, times(1)).authenticate(any());
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