package com.backend.usuario.service.impl;

import com.backend.usuario.repository.entity.UserEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserDetalheServiceImpl.class)
class UserDetalheServiceImplTest {

    @InjectMocks
    private UserDetalheServiceImpl userDetalheService;

    @MockBean
    private UserRepository userRepository;
    private UserEntity userEntity;

    @BeforeEach
    void setUp(){
        userDetalheService = new UserDetalheServiceImpl(userRepository);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Arrange
        String username = "testuser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        Optional<UserEntity> userEntityOptional = Optional.of(userEntity);

        when(userRepository.findByUsername(username)).thenReturn(userEntityOptional);

        // Act
        UserDetails userDetails = userDetalheService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "testuser";
        Optional<UserEntity> userEntityOptional = Optional.empty();

        when(userRepository.findByUsername(username)).thenReturn(userEntityOptional);

        // Act and Assert
        assertThrows(UserServiceException.class, () -> {
            userDetalheService.loadUserByUsername(username);
        });

        verify(userRepository, times(1)).findByUsername(username);
    }
}