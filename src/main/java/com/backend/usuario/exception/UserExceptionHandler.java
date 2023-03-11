package com.backend.usuario.exception;

import com.backend.usuario.constants.ErrorMessage;
import com.backend.usuario.domain.response.erro.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

public class UserExceptionHandler {
//    @ExceptionHandler(Exception.class)
//    public final ResponseEntity<Object> handleException(Exception exception, WebRequest webRequest){
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR));
//    }
//    @ExceptionHandler(NoSuchElementException.class)
//    public final ResponseEntity<Object> handleNoSuchElementException(Exception exception, WebRequest webRequest){
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ErrorResponse(HttpStatus.BAD_REQUEST, ErrorMessage.WRONGDATA));
//    }
//    @ExceptionHandler(ResponseStatusException.class)
//    public final ResponseEntity<Object> handleResultException(ResponseStatusException responseStatusException, WebRequest webRequest){
//        return ResponseEntity.status(responseStatusException.getStatus())
//                .body(new ErrorResponse(String.valueOf(responseStatusException.getStatus()), responseStatusException.getReason()));
//    }
}
