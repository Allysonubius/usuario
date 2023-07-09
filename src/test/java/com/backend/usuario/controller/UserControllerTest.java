package com.backend.usuario.controller;

import com.backend.usuario.domain.mapper.UserMapper;
import com.backend.usuario.domain.request.user.UserCreateUserRequest;
import com.backend.usuario.domain.response.erro.ErrorResponse;
import com.backend.usuario.domain.response.user.UserResponse;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserController.class)
class UserControllerTest {

    @MockBean
    private UserController userController;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @BeforeEach
    void setup(){
        this.userController = new UserController(
                userService,
                userMapper
        );
    }

    @Test
    void testSaveUserController_Success() {
        // Creating a mock UserCreateUserRequest
        UserCreateUserRequest createUserRequest = new UserCreateUserRequest();
        createUserRequest.setUsername("teste");
        createUserRequest.setPassword("teste");

        // Creating a mock User object
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(authority);
        UserEntity user = new UserEntity();

        // Creating a mock UserResponse object
        UserResponse userResponse = new UserResponse();

        // Mocking the UserMapper
        when(userMapper.toUserRequest(createUserRequest)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        // Mocking the UserService to return the saved user
        when(userService.saveUserService(user)).thenReturn(user);

        // Calling the API endpoint
        ResponseEntity<Object> response = userController.saveUserController(createUserRequest);

        // Verifying the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    public void testSaveUserController_InternalError() {
        // Creating a mock UserCreateUserRequest
        UserCreateUserRequest createUserRequest = new UserCreateUserRequest();
        createUserRequest.setUsername("teste");
        createUserRequest.setPassword("teste");

        // Creating a mock exception
        UserServiceException exception = new UserServiceException("Internal error");

        // Mocking the UserMapper

        // Creating a mock User object
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(authority);
        UserEntity user = new UserEntity();
        when(userMapper.toUserRequest(createUserRequest)).thenReturn(user);

        // Mocking the UserService to throw an exception
        when(userService.saveUserService(Mockito.any())).thenThrow(exception);

        // Calling the API endpoint
        ResponseEntity<Object> response = userController.saveUserController(createUserRequest);

        // Verifying the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Internal error", ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        UUID uuid = UUID.fromString("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3");

        doNothing().when(this.userService).deleteUser(uuid);

        ResponseEntity<Object> response = this.userController.deleteUser(uuid);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(null,response.getBody());
    }


    @Test
    void testDeleteUser_UserNotFound(){
        UUID userId = UUID.randomUUID();
        doThrow(new UserServiceException("Usuário não encontrado !")).when(userService).deleteUser(userId);

        ResponseEntity<Object> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Usuário não encontrado !", ((ErrorResponse) response.getBody()).getMessage());

    }

    @Test
    void testListUsers_Success() {
        // Mock the list of users
        List<UserResponse> responses = new ArrayList<>();
        responses.add(new UserResponse());
        Page<UserResponse> page = new PageImpl<>(responses);

        when(this.userService.listUsers(Mockito.any(Pageable.class))).thenReturn(page);

        Pageable pageable = PageRequest.of(0,10);

        ResponseEntity<Page<UserResponse>> response = this.userController.listUsers(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page,response.getBody());
    }

    @Test
    void testListUsers_NotFound(){
        when(this.userService.listUsers(Mockito.any(Pageable.class))).thenThrow(new UserServiceException(""));

        Pageable pageable = PageRequest.of(0, 10);

        ResponseEntity<Page<UserResponse>> response = userController.listUsers(pageable);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }
}