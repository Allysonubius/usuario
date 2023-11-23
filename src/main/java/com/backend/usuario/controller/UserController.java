package com.backend.usuario.controller;

import com.backend.usuario.domain.mapper.UserMapper;
import com.backend.usuario.domain.request.dados.UserDadosRequest;
import com.backend.usuario.domain.request.user.UserCreateUserRequest;
import com.backend.usuario.domain.response.erro.ErrorResponse;
import com.backend.usuario.domain.response.user.UserResponse;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
@Slf4j
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Api(value = "API REST User")
@ControllerAdvice
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * @param userCreateUserRequest
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario criado com sucesso !"),
            @ApiResponse(code = 403, message = "Você não tem permissão !", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "Nome ja cadastradado !", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação.", response = ErrorResponse.class),
    })
    @PostMapping(value = "/save-user")
    @ApiOperation(
            value = "API REST - Create USER",
            response = UserResponse.class
    )
    public ResponseEntity<Object> saveUserController(@Valid @RequestBody UserCreateUserRequest userCreateUserRequest){
        try{
            log.info("saveUserController() -Init saveUser");
            Optional<UserResponse> optional = Stream.of(userCreateUserRequest)
                    .map(this.userMapper::toUserRequest)
                    .map(this.userService::saveUserService)
                    .map( this.userMapper::toUserResponse)
                    .findFirst();
            if (optional.isEmpty()){
                log.info("saveUserController() - No data to save not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),"No data to save not found !","/api/save-user", LocalDateTime.now()));
            }
            log.info("saveUserController() - Finished saveUser");
            return ResponseEntity.status(HttpStatus.CREATED).body(optional.get());
        } catch (UserServiceException e){
            log.info("saveUserController() - Internal error when saving user " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(),"/api/save-user", LocalDateTime.now()));
        }
    }
    /**
     * @param id
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuario deletado com sucesso !"),
            @ApiResponse(code = 400, message = "Usuário não encontrado !"),
            @ApiResponse(code = 403, message = "Você não tem permissão !"),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação."),
    })
    @DeleteMapping(value = "/delete-user/{id}")
    @ApiOperation(value = "API REST - Delete USER")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") UUID id){
       try{
           this.userService.deleteUser(id);
           return ResponseEntity.status(HttpStatus.OK).build();
       }catch (UserServiceException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(),"/api/login-user", LocalDateTime.now()));
       }
    }
    /**
     * @param pageable
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Listagem completa !"),
            @ApiResponse(code = 404, message = "Lista vazia !"),
            @ApiResponse(code = 403, message = "Você não tem permissão !"),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação."),
    })
    @GetMapping(value = "/list-user")
    @ApiOperation(value = "API REST - List Users")
    public ResponseEntity<Page<UserResponse>> listUsers(Pageable pageable){
        try{
            Page<UserResponse> listUsers =this.userService.listUsers(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(listUsers);
        }catch (UserServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/dados-usuario")
    public ResponseEntity<UserDadosRequest> getDadosUsuario(Principal principal) {

        if (!principal.getName().isEmpty()) {
            UserDadosRequest userDados = userService.getDadosUser(principal.getName());
            if (userDados != null) {
                return ResponseEntity.ok(userDados);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }
}