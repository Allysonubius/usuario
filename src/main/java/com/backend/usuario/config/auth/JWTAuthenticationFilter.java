package com.backend.usuario.config.auth;

import com.auth0.jwt.algorithms.Algorithm;
import com.backend.usuario.config.data.DetalherUserData;
import com.backend.usuario.domain.request.user.UserRequest;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.exception.JwtAuthenticationFilterException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager ;
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }
    public Authentication authentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, JwtAuthenticationFilterException {
        log.info("authentication() - Starting authentication ");
        try {
            UserEntity userRequest = new ObjectMapper().readValue(httpServletRequest.getInputStream(), UserEntity.class);
            log.info("authentication() - Finished authentication ");
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getUsername(),
                            userRequest.getPassword(),
                            new ArrayList<>())
            );
        }catch (IOException e){
            throw new JwtAuthenticationFilterException(e.getMessage());
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain, Authentication authentication)throws IOException{
        logger.info("successfulAuthentication() - Starting authentication ");
        DetalherUserData data = (DetalherUserData) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        String token = JWT.create()
                .withSubject(data.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
        String body = ((UserRequest) authentication.getPrincipal()).getUsername() + " " + token;
        logger.info("successfulAuthentication() - Finished Authentication ");
        httpServletResponse.getWriter().write(body);
        httpServletResponse.getWriter().flush();
    }
}
