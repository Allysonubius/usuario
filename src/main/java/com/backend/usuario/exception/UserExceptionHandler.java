package com.backend.usuario.exception;

import com.backend.usuario.domain.response.erro.ErrorResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 *
 */
@RestControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {
    @Bean
    public DefaultErrorAttributes errorAttributes() {
        // Hide exception field in the return object
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                return super.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults().excluding(ErrorAttributeOptions.Include.EXCEPTION));
            }
        };
    }

    @ExceptionHandler(UserServiceException.class)
    public void handleCustomException(HttpServletResponse res, ErrorResponse ex) throws IOException {
        res.sendError(ex.getStatus(), ex.getErrorMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
    }

    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong");
    }

}
