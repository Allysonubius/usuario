package com.backend.usuario.config.data.jwt;

import com.backend.usuario.constants.SecurityConstants;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.backend.usuario.constants.SecurityConstants.*;


/**
 *
 */
@Slf4j
@Configuration
public class JwtUtils {

    /**
     * @param authentication
     * @return
     */
    public String generateJwtToken(Authentication authentication) {
        log.info("generateJwtToken() - Starting generating json web token - authentication:[{}]", authentication);
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Object userPrincipal = authentication.getPrincipal();
        JwtBuilder jwt =  Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.toString())
                .setExpiration(new Date((System.currentTimeMillis() + EXPIRATION_TIME)))
                .signWith(SignatureAlgorithm.HS256, SECRET);
        log.info("generateJwtToken() - Finishing generating json web token - authentication:[{}]", authentication);
        return jwt.compact();
    }
}
