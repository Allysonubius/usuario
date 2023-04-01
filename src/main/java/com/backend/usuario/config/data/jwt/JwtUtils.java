package com.backend.usuario.config.data.jwt;

import com.backend.usuario.domain.request.user.UserLoginRequest;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class JwtUtils {

    private final UserRepository userRepository;

    /**
     * @param authentication
     * @param user
     * @return
     */
    public String generateJwtToken(Authentication authentication, UserLoginRequest user) {
        log.info("generateJwtToken() - Starting generating json web token - authentication:[{}]", authentication);
        Optional<UserEntity> optionalUser = this.userRepository.findByUsername(user.getUsername());
        if(!optionalUser.isPresent()){
            log.info("generateJwtToken() - ");
            throw new UserServiceException("");
        }
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put("auth", optionalUser.stream().map(s -> new SimpleGrantedAuthority(s.getRole().getRole())).filter(Objects::nonNull).collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRATION_TIME);

        Object userPrincipal = authentication.getPrincipal();
        JwtBuilder jwt =  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setSubject(userPrincipal.toString())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, SECRET);
        log.info("generateJwtToken() - Finishing generating json web token - authentication:[{}]", authentication);

        return jwt.compact();
    }

    /**
     * @param username
     * @param user
     * @return
     */
    public String createToken(String username, Optional<UserEntity> user) {
        log.info("generateJwtToken() - Starting generating json web token - user:[{}]", user);
        if(user.isPresent()){
            log.info("");
            throw new UserServiceException("");
        }

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", user.stream().map(s -> new SimpleGrantedAuthority(s.getRole().getRole())).filter(Objects::nonNull).collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRATION_TIME);

       JwtBuilder jwtBuilder = Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, SECRET);

        log.info("generateJwtToken() - Finishing generating json web token - user:[{}]", user);

       return jwtBuilder.compact();
    }

}
