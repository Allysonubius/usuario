package com.backend.usuario.domain.mapper;

import com.backend.usuario.domain.request.role.RoleUserRequest;
import com.backend.usuario.domain.request.user.UserCreateUserRequest;
import com.backend.usuario.domain.response.user.UserResponse;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.meta.When;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserMapper.class)
class UserMapperTest {
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private UserService userService;
    @BeforeEach
    void setup(){
        userMapper = new UserMapper(
                modelMapper,
                bCryptPasswordEncoder,
                userService);
    }

    @Test
    void testToUserResponse(){
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.fromString("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3"));
        userEntity.setUsername("teste");
        userEntity.setEmail("teste@gmail.com");

        UserResponse expectedResponse = new UserResponse();
        expectedResponse.setId("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3");
        expectedResponse.setUsername(userEntity.getUsername());
        expectedResponse.setEmail(userEntity.getEmail());

        this.userMapper.toUserResponse(userEntity);

        assertEquals("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3",expectedResponse.getId());
        assertEquals("teste",expectedResponse.getUsername());
        assertEquals("teste@gmail.com",expectedResponse.getEmail());
    }

    @Test
    void toUserRequest() {
        UserCreateUserRequest request = new UserCreateUserRequest();
        request.setUsername("teste");
        request.setId(UUID.fromString("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3"));
        request.setEmail("teste@gmail.com");
        request.setPassword("secret");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3")); // Definido GMT-3 como horário de Brasília

        request.setDateCreate(dateFormat.format(new Date()));
        request.setDateUpdate(dateFormat.format(new Date()));

        RoleUserRequest roleUserRequest = new RoleUserRequest();
        roleUserRequest.setId(100L);
        request.setRole(roleUserRequest);

        when(this.userService.getRoleById(request.getRole().getId())).thenReturn(new RoleUserRequest());

        this.userMapper.toUserRequest(request);

        assertEquals("teste",request.getUsername());
        assertEquals("teste@gmail.com",request.getEmail());

        assertNotNull("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3");
    }

    @Test
    void testToUserRequest_EmptyUsernameOrPassword(){
        UserCreateUserRequest request = new UserCreateUserRequest();
        request.setUsername("");
        request.setPassword("");

        assertThrows(UserServiceException.class, () -> {
            this.userMapper.toUserRequest(request);
        });
    }

    @Test
    void testToUserRequest_validateEmailError(){
        UserCreateUserRequest request = new UserCreateUserRequest();
        request.setUsername("teste");
        request.setId(UUID.fromString("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3"));
        request.setEmail("testegmail.com");
        request.setPassword("secret");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3")); // Definido GMT-3 como horário de Brasília

        request.setDateCreate(dateFormat.format(new Date()));
        request.setDateUpdate(dateFormat.format(new Date()));

        assertThrows(UserServiceException.class , () ->{
            this.userMapper.toUserRequest(request);
        });
    }

    @Test
    void testToUserRequest_roleIdNotFound(){
        UserCreateUserRequest request = new UserCreateUserRequest();
        request.setUsername("teste");
        request.setId(UUID.fromString("bb39dcdd-fd0e-4135-9c2f-f30d4ce407d3"));
        request.setEmail("teste@gmail.com");
        request.setPassword("secret");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3")); // Definido GMT-3 como horário de Brasília

        request.setDateCreate(dateFormat.format(new Date()));
        request.setDateUpdate(dateFormat.format(new Date()));

        RoleUserRequest roleUserRequest = new RoleUserRequest();
        roleUserRequest.setId(100L);
        request.setRole(roleUserRequest);

        assertThrows(UserServiceException.class, () ->{
            this.userMapper.toUserRequest(request);
        });
    }
}