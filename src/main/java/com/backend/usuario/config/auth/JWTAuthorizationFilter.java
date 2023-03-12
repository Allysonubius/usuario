package com.backend.usuario.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.backend.usuario.config.data.jwt.JwtUtils;
import com.backend.usuario.exception.JwtAuthorizationFilterException;
import com.backend.usuario.service.impl.UserDetalheServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;

import static com.backend.usuario.constants.SecurityConstants.*;

@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    @Autowired
    private JwtUtils jwtUtils;
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
        String jwtToken = parseJwt(httpServletRequest);
        try {
            if(jwtToken != null  && validateToken(jwtToken) != null){
                UsernamePasswordAuthenticationToken authenticationToken = authenticationToken(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            log.info("doFilterInternal() - Error validation autorization user " + e.getMessage());
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(500 , e.getMessage());
        }
        log.info("doFilterInternal() - Finished validation autorization user " + jwtToken);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    private UsernamePasswordAuthenticationToken authenticationToken(String token) throws JwtAuthorizationFilterException {
        log.info("authenticationToken() - Starting authenticationToken " + token);
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        String usuario = JWT.require(algorithm)
                .build()
                .verify(token)
                .getSubject();
        if(usuario == null){
            log.info("authenticationToken() - User null " + null);
            throw new JwtAuthorizationFilterException("authenticationToken() - Error user null" + null);
        }
        log.info("authenticationToken() - Finished authenticationToken " + token);
        return new UsernamePasswordAuthenticationToken(usuario,null,new ArrayList<>());
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HEADER_STRING);
        if (headerAuth != null && StringUtils.hasText(headerAuth) && headerAuth.startsWith(TOKEN_PREFIX)) {
            return headerAuth.substring(7);
        }
        return null;
    }
    public Jws<Claims> validateToken(String token) throws JwtAuthorizationFilterException {
        log.info("validateToken() - Iniciado validação do token " +token);
        try {
            log.info("validateToken() - Finalizado validação do token " + token);
            return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        }catch (Exception e){
            log.info("validateToken() - Error ao validar o token " + e.getMessage());
            throw new JwtAuthorizationFilterException("validateToken() - Error ao validar o token " + e.getMessage());
        }
    }

}
