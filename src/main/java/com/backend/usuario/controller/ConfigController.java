package com.backend.usuario.controller;

import com.backend.usuario.domain.response.erro.ErrorResponse;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.service.ConfigAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Api(value = "API REST Config's User's")
@ControllerAdvice
public class ConfigController {

    private final ConfigAccountService configAccountService;
    /**
     * @param id
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario ativado com sucesso !"),
            @ApiResponse(code = 400, message = "Usuário não encontrado !"),
            @ApiResponse(code = 403, message = "Você não tem permissão !"),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação."),
    })
    @PostMapping(value = "/active-user/{id}")
    @ApiOperation(value = "API REST - Activate USER")
    public ResponseEntity<Object> activeAccount(@PathVariable("id") UUID id){
        try{
            this.configAccountService.getUserActiveAccount(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }catch (UserServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(),"/api/active-user", LocalDateTime.now()));
        }
    }

    /**
     * @param id
     * @return
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario desativado com sucesso !"),
            @ApiResponse(code = 400, message = "Usuário não encontrado !"),
            @ApiResponse(code = 403, message = "Você não tem permissão !"),
            @ApiResponse(code = 500, message = "O servidor encontrou uma condição inesperada que o impediu de atender à solicitação."),
    })
    @PostMapping(value = "/desactive-user/{id}")
    @ApiOperation(value = "API REST - Activate USER")
    public ResponseEntity<Object> desactiveAccount(@PathVariable("id") UUID id){
        try{
            this.configAccountService.getUserDesativeAccount(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }catch (UserServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(),e.getMessage(),"/api/active-user", LocalDateTime.now()));
        }
    }
}
