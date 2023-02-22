package com.backend.usuario.controller;

import com.backend.usuario.domain.mapper.UserMapper;
import com.backend.usuario.domain.request.user.UserRequest;
import com.backend.usuario.domain.response.user.UserResponse;
import com.backend.usuario.repository.UserRepository;
import com.backend.usuario.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Api(value = "API REST User")
@CrossOrigin(value = "*")
@Slf4j(topic = "UserController")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final UserMapper userMapper;
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario criado com sucesso !"),
            @ApiResponse(code = 403, message = "Você não tem permissão de inserir usuario !"),
            @ApiResponse(code = 404, message = "Nome ja cadastradado !"),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação."),
    })
    @PostMapping(value = "/user")
    @ApiOperation(value = "API REST - Criação de usuario")
    public ResponseEntity<UserResponse> saveUser(@Valid @RequestBody UserRequest userRequest){
        log.info("Init saveUser");
        Optional<UserResponse> optional = Stream.of(userRequest)
                .map(this.userMapper::toUserRequest)
                .map(this.userService::saveUserEntity)
                .map( this.userMapper::toUserResponse)
                .findFirst();
        return ResponseEntity.status(HttpStatus.CREATED).body(optional.get());
    }
}
