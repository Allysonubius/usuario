package com.backend.usuario.config.auth;

import com.auth0.jwt.algorithms.Algorithm;
import com.backend.usuario.domain.request.user.UserRequest;
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
import java.util.ArrayList;
import java.util.Date;

import com.auth0.jwt.JWT;

import static com.backend.usuario.constants.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager ;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl(SIGN_UP_URL);
    }
    public Authentication authentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)throws AuthenticationException{
        try {
            UserRequest userRequest = new ObjectMapper().readValue(httpServletRequest.getInputStream(), UserRequest.class);
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
        String token = JWT.create()
                .withSubject(((UserRequest) authentication.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
        String body = ((UserRequest) authentication.getPrincipal()).getUsername() + " " + token;

        httpServletResponse.getWriter().write(body);
        httpServletResponse.getWriter().flush();
    }
}
