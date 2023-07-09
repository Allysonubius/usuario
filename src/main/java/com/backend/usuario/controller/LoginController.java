package com.backend.usuario.controller;

import com.backend.usuario.domain.request.user.UserLoginRequest;
import com.backend.usuario.domain.response.erro.ErrorResponse;
import com.backend.usuario.domain.response.jwt.JwtResponse;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Api(value = "API REST Login User")
@ControllerAdvice
public class LoginController {
    private final LoginService loginService;
    /**
     * @param userLoginRequest
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario logado com sucesso !"),
            @ApiResponse(code = 403, message = "Você não tem permissão !"),
            @ApiResponse(code = 404, message = "Erro ao realizar login !"),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação."),
    })
    @PostMapping(value = "/login-user")
    @ApiOperation(value = "API REST - Login USER")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest){
        return this.loginService.loginUser(userLoginRequest);
    }
    /**
     * @param req
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Refresh token !"),
            @ApiResponse(code = 400, message = "Erro ao retornar token !"),
            @ApiResponse(code = 403, message = "Você não tem permissão a esta opção !"),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação."),
    })
    @GetMapping(value = "/refresh")
    @ApiOperation(value = "API REST - Refresh token")
    public ResponseEntity<Object> refreshUser(HttpServletRequest req){
        try{
            String token =this.loginService.refresh(req.getRemoteUser());
            return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(token));
        }catch (UserServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_GATEWAY.value(),e.getMessage(),"/api/refresh", LocalDateTime.now()));
        }
    }
}