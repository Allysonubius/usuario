package com.backend.usuario.constants;

public class SecurityConstants {
    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 1800000; // 15 minutes
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_USER_URL = "/api/login-user";
    public static final String CREATE_USER_URL = "/api/save-user";
    public static final String TOKEN_URL = "/api/token";
    public static final String SWAGGER_UI = "/swaggerr-ui.html";
    private SecurityConstants() {
    }
}
