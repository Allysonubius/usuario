package com.backend.usuario.exception;

public class JwtAuthenticationFilterException extends Exception{
    public JwtAuthenticationFilterException(String message){
        super(message);
    }
}
