package com.backend.usuario.config.auth;

import com.auth0.jwt.algorithms.Algorithm;
import com.backend.usuario.domain.request.user.UserRequest;
import com.backend.usuario.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

import com.auth0.jwt.JWT;


import static com.backend.usuario.constants.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager ;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }
    public Authentication authentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)throws AuthenticationException{
        try {
            UserEntity userRequest = new ObjectMapper().readValue(httpServletRequest.getInputStream(), UserEntity.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getUsername(),
                            userRequest.getPassword(),
                            new ArrayList<>())
            );
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain, Authentication authentication)throws IOException{
        Algorithm algorithm = Algorithm.HMAC512(SECRET.getBytes());
        String token = JWT.create()
                .withSubject(((UserRequest) authentication.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
        String body = ((UserRequest) authentication.getPrincipal()).getUsername() + " " + token;

        httpServletResponse.getWriter().write(body);
        httpServletResponse.getWriter().flush();
    }
}
