package com.backend.usuario.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.backend.usuario.config.data.jwt.JwtUtils;
import com.backend.usuario.exception.JwtAuthorizationFilterException;
import com.backend.usuario.service.impl.UserDetalheServiceImpl;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.util.*;

import static com.backend.usuario.constants.SecurityConstants.*;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private UserDetalheServiceImpl userDetalheServiceImpl;
    @Autowired
    private AuthenticationManager authenticationManager;
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException{
        log.info("doFilterInternal() - Starting validation autorization user " + httpServletRequest.getAuthType());
        String header = httpServletRequest.getHeader(HEADER_STRING);
        try {
            if (header == null || !header.startsWith(TOKEN_PREFIX)){
                filterChain.doFilter(httpServletRequest,httpServletResponse);
                return;
            }
            String token = header.replace(TOKEN_PREFIX,"");
            Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            String username = claims.getSubject();
            if(username == null){
                log.info("authenticationToken() - Username null " + null);
                throw new JwtAuthorizationFilterException("authenticationToken() - Error username null " + null);
            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(username,null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("doFilterInternal() - Finished validation autorization user " + httpServletRequest.getRequestURI());
        } catch (Exception e) {
            log.info("doFilterInternal() - Error validation autorization user " + e.getMessage());
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
