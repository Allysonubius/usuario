package com.backend.usuario.domain.response.erro;

import com.backend.usuario.constants.ErrorMessage;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {

    private static  String getErrorCodeAsString(HttpStatus httpStatus){
        return Integer.toString(httpStatus.value());
    }
    private String errorCode;
    private String errorMessage;
    public ErrorResponse(String errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    public ErrorResponse(HttpStatus httpStatus){
        this(getErrorCodeAsString(httpStatus),httpStatus.getReasonPhrase());
    }
    public ErrorResponse(HttpStatus httpStatus, ErrorMessage errorMessage){
        this(getErrorCodeAsString(httpStatus), errorMessage.getValue());
    }
}
