package com.backend.usuario.config.data.jwt;

import com.backend.usuario.constants.SecurityConstants;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static com.backend.usuario.constants.SecurityConstants.EXPIRATION_TIME;
import static com.backend.usuario.constants.SecurityConstants.SECRET;
@Slf4j
@Configuration
public class JwtUtils {

    public String generateJwtToken(Authentication authentication) {
        log.info("generateJwtToken() - Starting generating json web token ");
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Object userPrincipal = authentication.getPrincipal();
        String jwt =  Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date((System.currentTimeMillis() + EXPIRATION_TIME)))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        log.info("generateJwtToken() - Finished generating json web token ");
        return jwt;
    }
}
