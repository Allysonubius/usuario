package com.backend.usuario.domain.mapper;

import com.backend.usuario.domain.request.user.UserCreateUserRequest;
import com.backend.usuario.domain.response.user.UserResponse;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@SpringBootTest(classes = UserMapper.class)
class UserMapperTest {

    private UserService userService;
    @MockBean
    private ModelMapper modelMapper;
    private UserMapper userMapper;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserEntity userEntity;
    private UserCreateUserRequest userCreateUserRequest;

    @Before
    public void setUp() {

    }

    @Test
    public void testToUserRequest() {
//        // Arrange
//        UserRequest request = new UserRequest(UUID.randomUUID(),"allyson","1234567890",String.valueOf(new Date()),null);
//
//        // Act
//        modelMapper.map(request, UserEntity.class);
//
//        // Assert
//        assertEquals(request.getUsername(), "allyson");
//        assertFalse(bCryptPasswordEncoder.matches(request.getPassword(), request.getPassword()));
    }

    @Test
    public void testToUserResponse() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setUsername("allyson");
        userEntity.setDateCreate(new Date());

        // Act
        UserResponse result = userMapper.toUserResponse(userEntity);

        // Assert
        assertNotNull(result);
        assertEquals(userEntity.getId().toString(), result.getId());
        assertEquals(userEntity.getUsername(), result.getUsername());
        assertEquals(userEntity.getDateCreate(), result.getDateCreate());
        assertNull(result.getDateUpdate());
    }

}