package com.backend.usuario.controller;

import com.backend.usuario.domain.mapper.UserMapper;
import com.backend.usuario.domain.request.user.UserCreateUserRequest;
import com.backend.usuario.domain.request.user.UserLoginRequest;
import com.backend.usuario.domain.response.erro.ErrorResponse;
import com.backend.usuario.domain.response.jwt.JwtResponse;
import com.backend.usuario.domain.response.user.UserResponse;
import com.backend.usuario.repository.UserRepository;
import com.backend.usuario.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *
 */
@Slf4j
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Api(value = "API REST User")
@CrossOrigin(value = "*")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final UserMapper userMapper;

    /**
     * @param userCreateUserRequest
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario criado com sucesso !"),
            @ApiResponse(code = 403, message = "Você não tem permissão de inserir usuario !", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Nome ja cadastradado !", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação.", response = ErrorResponse.class),
    })
    @PostMapping(value = "/save-user")
    @ApiOperation(
            value = "API REST - Create USER",
            response = UserResponse.class
    )
    public ResponseEntity<UserResponse> saveUserController(@Valid @RequestBody UserCreateUserRequest userCreateUserRequest){
        try{
            log.info("saveUserController() -Init saveUser");
            Optional<UserResponse> optional = Stream.of(userCreateUserRequest)
                    .map(this.userMapper::toUserRequest)
                    .map(this.userService::saveUserService)
                    .map( this.userMapper::toUserResponse)
                    .findFirst();
            if (optional.isEmpty()){
                log.info("saveUserController() - No data to save not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserResponse());
            }
            log.info("saveUserController() - Finished saveUser");
            return ResponseEntity.status(HttpStatus.CREATED).body(optional.get());
        } catch (Exception e){
            log.info("saveUserController() - Internal error when saving user " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * @param userLoginRequest
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario criado com sucesso !"),
            @ApiResponse(code = 403, message = "Você não tem permissão de inserir usuario !"),
            @ApiResponse(code = 404, message = "Nome ja cadastradado !"),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação."),
    })
    @PostMapping(value = "/login-user")
    @ApiOperation(value = "API REST - Login USER")
    public ResponseEntity<JwtResponse> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest){
        return this.userService.loginUser(userLoginRequest);
    }
}
