package com.backend.usuario.exception;

public class UserServiceException extends RuntimeException{
    /**
     * @param message
     */
    public UserServiceException(String message){
        super(message);
    }
}
