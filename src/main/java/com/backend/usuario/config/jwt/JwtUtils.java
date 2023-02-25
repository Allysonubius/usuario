package com.backend.usuario.config.jwt;

import com.backend.usuario.constants.SecurityConstants;
import com.backend.usuario.entity.UserEntity;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

import java.util.Date;
@Configuration
public class JwtUtils {
    private SecurityConstants securityConstants;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    public String generateJwtToken(Authentication authentication) {
        UserEntity userPrincipal = (UserEntity) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + securityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, securityConstants.SECRET)
                .compact();
    }
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(securityConstants.SECRET).parseClaimsJws(token).getBody().getSubject();
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(securityConstants.SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
