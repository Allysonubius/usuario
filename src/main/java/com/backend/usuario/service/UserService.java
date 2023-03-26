package com.backend.usuario.service;

import com.backend.usuario.config.data.jwt.JwtUtils;
import com.backend.usuario.domain.request.user.UserLoginRequest;
import com.backend.usuario.domain.response.jwt.JwtResponse;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

/**
 *
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    /*
     *
     */
    @SneakyThrows
    public UserEntity saveUserService(UserEntity userEntity){
        try{
            Optional<UserEntity> optionalUsername = this.userRepository.findByUsername(userEntity.getUsername());
            Optional<UserEntity> optionalEmail = this.userRepository.findByEmail(userEntity.getEmail());
            if(optionalUsername.isPresent()){
                log.info("saveUserService() -Usuário ja cadastrado - " + userEntity.getUsername());
                throw new UserServiceException("saveUserService() - Username já cadastrado : " + userEntity.getUsername());
            }
            if(optionalEmail.isPresent()){
                log.info("saveUserService() - Email ja cadastrado - " + userEntity.getEmail());
                throw new UserServiceException("saveUserService() - Email já cadastrado : " + userEntity.getEmail());
            }
        } catch (Exception e){
            log.info("saveUserService() - Internal error when saving user " + e.getMessage());
            throw new UserServiceException("saveUserService() - Internal error when saving user " + e.getMessage());
        }
        return this.userRepository.save(userEntity);
    }

    /**
     * @param user
     * @return
     */
    public ResponseEntity<JwtResponse> loginUser(@Valid UserLoginRequest user){
       try {
           log.info("loginUser() - Starting login user - user:[{}]", user);
           Authentication authentication = this.authenticationManager.authenticate(authenticationToken(user.getUsername(), user.getPassword()));
           SecurityContextHolder.getContext().setAuthentication(authentication);
           String jwt = jwtUtils.generateJwtToken(authentication);

           log.info("loginUser() - Finishing login user - user:[{}]", user);
           return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwt));
       }catch (Exception e){
           log.info("loginUser() - Error login user - message:[{}]", e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
    }

    /**
     * @param username
     * @param password
     * @return
     */
    private UsernamePasswordAuthenticationToken authenticationToken(String username, String password){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);
        return token;
    }
}
