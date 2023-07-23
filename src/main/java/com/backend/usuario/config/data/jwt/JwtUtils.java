package com.backend.usuario.config.data.jwt;

import com.backend.usuario.domain.request.user.UserLoginRequest;
import com.backend.usuario.repository.entity.UserEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

import static com.backend.usuario.domain.constants.SecurityConstants.*;

@Slf4j
@Configuration
@AllArgsConstructor
public class JwtUtils {
    private final UserRepository userRepository;
    /**
     * @param authentication
     * @param user
     * @return
     */
    public String generateJwtToken(Authentication authentication, UserLoginRequest user) {
        log.info("generateJwtToken() - Starting generating JWT - user:{} ", user.getUsername());
        Optional<UserEntity> optionalUser = this.userRepository.findByUsername(user.getUsername());
        if(!optionalUser.isPresent()){
            log.info("generateJwtToken() - Failed to generate JWT token. User not found with username: {}", user.getUsername());
            throw new UserServiceException("Failed to generate JWT token. User not found with username: {}" + user.getUsername());
        }

        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put("auth", optionalUser.stream().map(s -> new SimpleGrantedAuthority(s.getRole().getRole())).filter(Objects::nonNull).toList());

        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRATION_TIME);

        Object userPrincipal = authentication.getPrincipal();
        JwtBuilder jwt =  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setSubject(userPrincipal.toString())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET);
        log.info("generateJwtToken() - Finishing generating json web token - authentication:{}", authentication.isAuthenticated());

        return jwt.compact();
    }

    /**
     * @param username
     * @param user
     * @return
     */
    public String createToken(String username, Optional<UserEntity> user) {
        log.info("createToken() - Starting generation of JSON web token for user: {}", username);
        if(user.isPresent()){
            log.error("createToken() - An error occurred while generating the JSON web token for username: {}", username);
            throw new UserServiceException("An error occurred while generating the JSON web token for username: " + username);
        }

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", user.stream().map(s -> new SimpleGrantedAuthority(s.getRole().getRole())).filter(Objects::nonNull).toList());

        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRATION_TIME);

       JwtBuilder jwtBuilder = Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, SECRET);

        log.info("createToken() - Finished generating JSON web token for username: {}", username);

        return jwtBuilder.compact();
    }
}
