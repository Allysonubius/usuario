package com.backend.usuario.exception;

/**
 *
 */
public class JwtAuthorizationFilterException extends Exception{
    /**
     * @param message
     */
    public JwtAuthorizationFilterException(String message){
        super(message);
    }
}
