package com.backend.usuario.service;

import com.backend.usuario.config.data.jwt.JwtUtils;
import com.backend.usuario.controller.UserController;
import com.backend.usuario.domain.request.role.RoleUserRequest;
import com.backend.usuario.domain.request.user.UserCreateUserRequest;
import com.backend.usuario.domain.request.user.UserLoginRequest;
import com.backend.usuario.domain.response.erro.ErrorResponse;
import com.backend.usuario.domain.response.jwt.JwtResponse;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.entity.UserRoleEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import com.backend.usuario.repository.UserRoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Optional;
import java.util.UUID;

import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    @BeforeEach
    void setup(){
        userService = new UserService(userRepository,userRoleRepository,jwtUtils,authenticationManager);
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
        ResponseEntity<Object> response = this.userService.loginUser(userLoginRequest);

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
        ResponseEntity<Object> response = this.userService.loginUser(user);

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
    void saveUserShouldSaveSuccessfully(){
        when(userRepository.findByUsername(createUser().getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(createUser().getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(createUser());

        UserEntity savedUser = this.userService.saveUserService(createUser());

        Assertions.assertThat(savedUser).isEqualTo(createUser());
    }
    @Test
    void saveUserShouldThrowDataIntegrityViolationExceptionWhenUsernameAlreadyExists() {
        when(userRepository.findByUsername(createUser().getUsername())).thenReturn(Optional.of(createUser()));

        assertThatThrownBy(() ->
                userService.saveUserService(createUser()))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Error when saving user: Username already registered - username: john.doe");
    }
    @Test
    void saveUserShouldThrowDataIntegrityViolationExceptionWhenEmailAlreadyExists() {
        when(userRepository.findByUsername(createUser().getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(createUser().getEmail())).thenReturn(Optional.of(createUser()));

        assertThatThrownBy(() ->
                userService.saveUserService(createUser()))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Error when saving user: Email already registered - email: john.doe@example.com");
    }
    @Test
    void saveUserShouldThrowUserServiceExceptionWhenUserServiceExceptionIsThrown() {
        when(userRepository.findByUsername(createUser().getUsername())).thenThrow(new UserServiceException("Error"));

        assertThatThrownBy(() -> userService.saveUserService(createUser()))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Error");
    }
    @Test
    void saveUserShouldThrowUserServiceExceptionWhenUnknownExceptionIsThrown() {
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
    @Test
    void testRefresh_success() {
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
    void testRefresh_failure() {
        String username = "";

        assertThatThrownBy(() ->
                this.userService.refresh(username))
                .isInstanceOf(UserServiceException.class)
                .hasMessageContaining("Username is empty");

        verify(userRepository, never()).findByUsername(anyString());
        verify(jwtUtils, never()).createToken(anyString(), any());
    }
    @Test
    void  testGetRoleById(){
        Long id = 1L;
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setId(id);
        RoleUserRequest expectedRoleUser = new RoleUserRequest();
        expectedRoleUser.setId(id);

        when(this.userRoleRepository.findById(id)).thenReturn(Optional.of(userRoleEntity));

        RoleUserRequest actualRoleUser = this.userService.getRoleById(id);

        assertEquals(expectedRoleUser.getId(), actualRoleUser.getId());
    }
    @Test
    void testGetRoleById_RoleNotFound(){
        Long id = 1L;

        when(this.userRoleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserServiceException.class, () -> {
            this.userService.getRoleById(id);
        });

        verify(userRoleRepository, times(1)).findById(id);
    }
    @Test
    void testDeleteUser(){
        UUID uuid = UUID.randomUUID();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(uuid);
        Optional<UserEntity> user = Optional.of(userEntity);
        when(this.userRepository.findById(uuid)).thenReturn(user);
        assertDoesNotThrow(() -> {
            this.userService.deleteUser(uuid);
        });

        verify(userRepository, times(1)).deleteById(uuid);
    }
    @Test
    void testDeleteUser_UserServiceException() {
        UUID id = UUID.randomUUID();

        UserRepository userRepository = mock(UserRepository.class);
        doThrow(new UserServiceException("Failed to delete user ID: " + id)).when(userRepository).deleteById(id);

        UserServiceException exception = assertThrows(UserServiceException.class, () -> {
            this.userService.deleteUser(id);
        });
        assertEquals("Failed to delete user ID: " + id, exception.getMessage());
    }
    private UserEntity createUser() {
        UserEntity user = new UserEntity();
        user.setUsername("john.doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");

        return user;
    }
}