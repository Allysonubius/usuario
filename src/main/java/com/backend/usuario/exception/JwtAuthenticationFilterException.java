package com.backend.usuario.exception;

/**
 *
 */
public class JwtAuthenticationFilterException extends Exception{
    /**
     * @param message
     */
    public JwtAuthenticationFilterException(String message){
        super(message);
    }
}
