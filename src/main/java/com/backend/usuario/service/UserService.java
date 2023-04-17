package com.backend.usuario.service;

import com.backend.usuario.config.data.jwt.JwtUtils;
import com.backend.usuario.domain.request.role.RoleUserRequest;
import com.backend.usuario.domain.request.user.UserLoginRequest;
import com.backend.usuario.domain.response.erro.ErrorResponse;
import com.backend.usuario.domain.response.jwt.JwtResponse;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.entity.UserRoleEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import com.backend.usuario.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@ControllerAdvice
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    /*
     *
     */
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

            return this.userRepository.save(userEntity);
        } catch (UserServiceException e){
            log.info("saveUserService() - Internal error when saving user - message:[{}]" + e.getMessage());
            throw new UserServiceException("saveUserService() - Internal error " + e.getMessage());
        }
    }

    /**
     * @param user
     * @return
     */
    public ResponseEntity<Object> loginUser(@Valid UserLoginRequest user){
       try {
           log.info("loginUser() - Starting login user - user:[{}]", user);
           Authentication authentication = this.authenticationManager.authenticate(authenticationToken(user.getUsername(), user.getPassword()));
           SecurityContextHolder.getContext().setAuthentication(authentication);
           String jwt = jwtUtils.generateJwtToken(authentication,user);

           log.info("loginUser() - Finishing login user - user:[{}]", user);
           return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwt));
       }catch (UserServiceException e){
           log.info("loginUser() - Error login user - message:[{}]", e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_GATEWAY.value(),e.getMessage(),e.getLocalizedMessage(), LocalDateTime.now()));
       }
    }

    public void deleteUser(UUID id){
        try {
            Optional<UserEntity> userId = this.userRepository.findById(id);
            if(!userId.isPresent()){
                log.info("deleteUser() - ");
                throw new UserServiceException("saveUserService() - Internal error when saving user " );
            }
            this.userRepository.deleteById(id);
        }catch (UserServiceException e){
            log.info("");
            throw new UserServiceException("saveUserService() - Internal error when saving user " + e.getMessage());
        }
    }

    public String refresh(String username) {
        log.info("loginUser() - Starting login user - user:[{}]", username);
        return jwtUtils.createToken(username,this.userRepository.findByUsername(username));
    }

    public RoleUserRequest getRoleById(Long id) {
        Optional<UserRoleEntity> userRoleOpt = this.userRoleRepository.findById(id);
        if (userRoleOpt.isPresent()) {
            UserRoleEntity userRole = userRoleOpt.get();
            RoleUserRequest roleUser = new RoleUserRequest();
            roleUser.setId(userRole.getId());
            return roleUser;
        } else {
            log.info("getRoleById() - role not found");
            throw new UserServiceException("getRoleById() - role not found");
        }
    }


    /**
     * @param username
     * @param password
     * @return
     */
    private UsernamePasswordAuthenticationToken authenticationToken(String username, String password){
        return new UsernamePasswordAuthenticationToken(username,password);
    }
}
