package com.backend.usuario.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.backend.usuario.constants.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException{
        String header = httpServletRequest.getHeader(HEADER_STRING);
        if(header == null || !header.startsWith(TOKEN_PREFIX)){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = this.getAuthentication(httpServletRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        logger.info("Inside Once Per Request Filter originated by request {}" + httpServletRequest.getRequestURI());
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HEADER_STRING);
        if(token != null || token.isEmpty()){
            // Parse the token
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();
            if (user != null || user.isEmpty()){
                // New arrayList means authorities
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }else {
                throw new RuntimeException("User " + httpServletRequest.getRequestURI());
            }
        }else{
            throw new RuntimeException("User " + httpServletRequest.getRequestURI());
        }
    }
}
