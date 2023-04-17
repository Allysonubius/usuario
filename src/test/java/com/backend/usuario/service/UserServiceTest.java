package com.backend.usuario.service;

import com.backend.usuario.config.data.jwt.JwtUtils;
import com.backend.usuario.controller.UserController;
import com.backend.usuario.domain.request.user.UserCreateUserRequest;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Date;
import java.util.Optional;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UserService.class)
class UserServiceTest {
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    private UserController userController;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;

    private UserCreateUserRequest userCreateUserRequest;

    // UserEntity userEntity = new UserEntity(UUID.randomUUID(),username,"1234567890",new Date(),null);

    @Test
    void testSaveUserService_UserNotExist() {
        // Creating a test user
//        String username = "teste";
//        UserEntity userEntity = new UserEntity(UUID.randomUUID(),username,"1234567890",new Date(),null);
//
//        when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.empty());
//        when(userRepository.save(userEntity)).thenReturn(userEntity);
//
//        assertEquals(userEntity, this.userRepository.save(userEntity));
    }

}