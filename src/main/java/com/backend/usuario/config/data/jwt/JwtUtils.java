package com.backend.usuario.config.data.jwt;

import com.backend.usuario.constants.SecurityConstants;
import io.jsonwebtoken.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

import static com.backend.usuario.constants.SecurityConstants.SECRET;

@Configuration
public class JwtUtils {
    private SecurityConstants securityConstants;

    @PostConstruct
    protected void init(){
        SECRET = Base64.getEncoder().encodeToString(SECRET.getBytes());
    }
    public String generateJwtToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Object userPrincipal = authentication.getPrincipal();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userPrincipal.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + securityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
}
