package com.backend.usuario.exception;

public class JwtAuthorizationFilterException extends Exception{
    public JwtAuthorizationFilterException(String message){
        super(message);
    }
}
