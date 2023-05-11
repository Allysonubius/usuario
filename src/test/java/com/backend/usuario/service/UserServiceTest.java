package com.backend.usuario.service;

import com.backend.usuario.config.data.jwt.JwtUtils;
import com.backend.usuario.controller.UserController;
import com.backend.usuario.domain.request.user.UserCreateUserRequest;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import com.backend.usuario.repository.UserRoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Optional;
import java.util.UUID;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

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
    private UserRoleRepository userRoleRepository;
    @MockBean
    private UserService userService;
    private UserCreateUserRequest userCreateUserRequest;
    @BeforeEach
    void setup(){
        userService = new UserService(userRepository,userRoleRepository,jwtUtils,authenticationManager);
    }
    @Test
    void saveUserShouldSaveSuccessfully(){
        when(userRepository.findByUsername(createUser().getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(createUser().getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(createUser());

        UserEntity savedUser = this.userService.saveUserService(createUser());

        Assertions.assertThat(savedUser).isEqualTo(createUser());
    }
    @Test
    public void saveUserShouldThrowDataIntegrityViolationExceptionWhenUsernameAlreadyExists() {
        when(userRepository.findByUsername(createUser().getUsername())).thenReturn(Optional.of(createUser()));

        assertThatThrownBy(() ->
                userService.saveUserService(createUser()))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Error when saving user: Username already registered - username: john.doe");
    }
    @Test
    public void saveUserShouldThrowDataIntegrityViolationExceptionWhenEmailAlreadyExists() {
        when(userRepository.findByUsername(createUser().getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(createUser().getEmail())).thenReturn(Optional.of(createUser()));

        assertThatThrownBy(() ->
                userService.saveUserService(createUser()))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Error when saving user: Email already registered - email: john.doe@example.com");
    }
    @Test
    public void saveUserShouldThrowUserServiceExceptionWhenUserServiceExceptionIsThrown() {
        when(userRepository.findByUsername(createUser().getUsername())).thenThrow(new UserServiceException("Error"));

        assertThatThrownBy(() -> userService.saveUserService(createUser()))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Error");
    }
    @Test
    public void saveUserShouldThrowUserServiceExceptionWhenUnknownExceptionIsThrown() {
        when(userRepository.findByUsername(createUser().getUsername())).thenThrow(new RuntimeException("Error"));

        assertThatThrownBy(() -> userService.saveUserService(createUser()))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Unknown error");
    }
    @Test
    void testDeleteUserSearch_sucess(){
        UUID uuid = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(uuid);
        Optional<UserEntity> user = Optional.of(userEntity);
        when(this.userRepository.findById(uuid)).thenReturn(user);
        this.userService.deleteUserSearch(uuid);
        verify(this.userRepository, timeout(1)).delete(userEntity);
    }
    @Test
    void testDeleteUserSearch_failure(){
        UUID uuid = UUID.fromString("95da51e8-d8bb-4370-8e9b-e33af89e780d");
        Optional<UserEntity> user = Optional.empty();
        when(this.userRepository.findById(uuid)).thenReturn(user);
        assertThatThrownBy(() ->
                this.userService.deleteUserSearch(uuid))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("User not found for ID: 95da51e8-d8bb-4370-8e9b-e33af89e780d");
    }
    private UserEntity createUser() {
        UserEntity user = new UserEntity();
        user.setUsername("john.doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");

        return user;
    }

    @Test
    public void testRefresh_success() {
        String username = "testuser";
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(jwtUtils.createToken(username, Optional.of(userEntity))).thenReturn("testtoken");

        String token = userService.refresh(username);

        assertNotNull(token);
        assertEquals("testtoken", token);
        verify(userRepository, times(1)).findByUsername(username);
        verify(jwtUtils, times(1)).createToken(username, Optional.of(userEntity));
    }
    @Test
    public void testRefresh_failure() {
        String username = "";

        assertThatThrownBy(() ->
                this.userService.refresh(username))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Username is empty");

        verify(userRepository, never()).findByUsername(anyString());
        verify(jwtUtils, never()).createToken(anyString(), any());
    }
}