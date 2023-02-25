package com.backend.usuario.controller;

import com.backend.usuario.config.jwt.JwtUtils;
import com.backend.usuario.domain.mapper.UserMapper;
import com.backend.usuario.domain.request.user.UserRequest;
import com.backend.usuario.domain.response.jwt.JwtResponse;
import com.backend.usuario.domain.response.user.UserResponse;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.repository.UserRepository;
import com.backend.usuario.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Api(value = "API REST User")
@CrossOrigin(value = "*")
public class UserController {
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private final UserMapper userMapper;
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario criado com sucesso !"),
            @ApiResponse(code = 403, message = "Você não tem permissão de inserir usuario !"),
            @ApiResponse(code = 404, message = "Nome ja cadastradado !"),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação."),
    })
    @PostMapping(value = "/saveUser")
    @ApiOperation(value = "API REST - Criação de usuario")
    public ResponseEntity<UserResponse> saveUser(@Valid @RequestBody UserRequest userRequest){
        LOGGER.info("Init saveUser");
        Optional<UserResponse> optional = Stream.of(userRequest)
                .map(this.userMapper::toUserRequest)
                .map(this.userService::saveUserEntity)
                .map( this.userMapper::toUserResponse)
                .findFirst();
        LOGGER.info("Finished saveUser");
        return ResponseEntity.status(HttpStatus.CREATED).body(optional.get());
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario criado com sucesso !"),
            @ApiResponse(code = 403, message = "Você não tem permissão de inserir usuario !"),
            @ApiResponse(code = 404, message = "Nome ja cadastradado !"),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação."),
    })
    @PostMapping(value = "/token")
    @ApiOperation(value = "API REST - Criação de usuario")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserRequest userRequest){
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userRequest.getUsername(),userRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwt));
    }
}
