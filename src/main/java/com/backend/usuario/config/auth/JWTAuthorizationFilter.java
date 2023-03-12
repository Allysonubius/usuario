package com.backend.usuario.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.backend.usuario.config.data.jwt.JwtUtils;
import com.backend.usuario.service.impl.UserDetalheServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
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
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }
    @PostConstruct
    protected void init(){
        SECRET = Base64.getEncoder().encodeToString(SECRET.getBytes());
    }
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException{
        String jwtToken = parseJwt(httpServletRequest);
        try {
            if(jwtToken != null  && validateToken(jwtToken)){
                Authentication authentication = getUsernamePasswordAuthenticationToken(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(500 , e.getMessage());
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getSubject();
    }
    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token){
        String usuario = JWT.require(Algorithm.HMAC512(token))
                .build()
                .verify(token)
                .getSubject();
        if(usuario == null){
            return null;
        }
        return new UsernamePasswordAuthenticationToken(usuario,null,new ArrayList<>());
    }
    private Authentication getAuthentication(String token){
        UserDetails userDetails = this.userDetalheServiceImpl.loadUserByUsername(getUsernameFromJwtToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HEADER_STRING);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(TOKEN_PREFIX)) {
            return headerAuth.substring(7);
        }
        return null;
    }
    public boolean validateToken(String token){
        log.info("validateToken() - Iniciado validação do arquivo");
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            log.info("validateToken() - Finalizado validação do arquivo");
            return true;
        }catch (Exception e){
            log.info("validateToken() - Error ao validar o token");
            throw new RuntimeException("" + e.getMessage());
        }
    }

}
