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

}