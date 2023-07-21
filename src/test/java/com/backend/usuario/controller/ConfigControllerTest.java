package com.backend.usuario.controller;

import com.backend.usuario.domain.response.erro.ErrorResponse;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.entity.UserRoleEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import com.backend.usuario.service.ConfigAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes =  ConfigController.class)
class ConfigControllerTest {
    @InjectMocks
    private ConfigController configController;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ConfigAccountService configAccountService;

    @BeforeEach
    void setup(){
        this.configController = new ConfigController(
                configAccountService
        );
    }

    @Test
    void activeAccount_Sucess() {
        UUID uuid = UUID.fromString("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3");

        UserEntity user1 = new UserEntity();
        user1.setId(uuid);
        user1.setUsername("john_doe");
        user1.setPassword("teste");
        user1.setDateCreate(new Date());
        user1.setDateUpdate(new Date());
        user1.setEmail("john.doe@example.com");
        user1.setActive("false");
        Long id = 1L;
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setId(id);
        user1.setRole(userRoleEntity);

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user1));

        ResponseEntity<Object> response = this.configController.activeAccount(uuid);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    void activeAccount_ExceptionBadRequest(){
        UUID userId = UUID.fromString("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3");

        doThrow(new UserServiceException("Usuário não encontrado !")).when(configAccountService).getUserActiveAccount(userId);

        ResponseEntity<Object> response = this.configController.activeAccount(userId);

        // Verifica se a resposta tem o status HTTP_BAD_REQUEST (400)
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verifica se a resposta contém a mensagem de erro esperada
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("Usuário não encontrado !", errorResponse.getMessage());
        assertEquals("/api/active-user", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void desactiveAccount_Success() {
        UUID uuid = UUID.fromString("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3");

        UserEntity user1 = new UserEntity();
        user1.setId(uuid);
        user1.setUsername("john_doe");
        user1.setPassword("teste");
        user1.setDateCreate(new Date());
        user1.setDateUpdate(new Date());
        user1.setEmail("john.doe@example.com");
        user1.setActive("false");
        Long id = 1L;
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setId(id);
        user1.setRole(userRoleEntity);

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user1));

        ResponseEntity<Object> response = this.configController.desactiveAccount(uuid);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    void desactiveAccount_ExceptionBadRequest(){
        UUID userId = UUID.fromString("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3");

        // Simula o comportamento do configAccountService.getUserDesativeAccount()
        // para lançar uma UserServiceException
        doThrow(new UserServiceException("Usuário não encontrado !")).when(configAccountService).getUserDesativeAccount(userId);

        ResponseEntity<Object> response = this.configController.desactiveAccount(userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals("Usuário não encontrado !", errorResponse.getMessage());
        assertEquals("/api/active-user", errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }
}