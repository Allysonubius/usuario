package com.backend.usuario.service;

import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ConfigAccountService.class)
class ConfigAccountServiceTest {
    @InjectMocks
    private ConfigAccountService configAccountService;
    @MockBean
    private UserRepository userRepository;
    @BeforeEach
    void setup(){
        configAccountService = new ConfigAccountService(userRepository);
    }

    @Test
    void testGetUserActiveAccount_ActivateUserAccount() {
        UUID userId = UUID.fromString("7c30d466-3e57-45d9-b8fb-e25379edfb80");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setActive("false");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserEntity result = configAccountService.getUserActiveAccount(userId);

        assertEquals(userId, result.getId());
        assertEquals("true", result.getActive());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void testGetUserActiveAccount_UserNotFound() {
        UUID userId = UUID.fromString("7c30d466-3e57-45d9-b8fb-e25379edfb80");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserServiceException.class, () -> configAccountService.getUserActiveAccount(userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void testGetUserActiveAccount_UserAccountAlreadyActive() {
        UUID userId = UUID.fromString("7c30d466-3e57-45d9-b8fb-e25379edfb80");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setActive("true");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        assertThrows(UserServiceException.class, () -> configAccountService.getUserActiveAccount(userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(UserEntity.class));
    }
}