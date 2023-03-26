package com.backend.usuario.config.data.jwt;

import com.backend.usuario.entity.UserEntity;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

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

    public String createToken(String username, Optional<UserEntity> user) {
        if(user.isPresent()){
            log.info("");
            throw new RuntimeException("");
        }

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", user.stream().map(s -> new SimpleGrantedAuthority(s.getUsername())).filter(Objects::nonNull).collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, SECRET)//
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Expired or invalid JWT token");
        }
    }
}
