package com.backend.usuario.service;

import com.backend.usuario.config.data.jwt.JwtUtils;
import com.backend.usuario.domain.request.user.UserRequest;
import com.backend.usuario.domain.response.jwt.JwtResponse;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j
@Service
@Transactional
public class UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;

    /*
     * JavaDoc
     */
    public UserEntity saveUserService(UserEntity userEntity){
        try{
            Optional<UserEntity> optional = this.userRepository.findByUsername(userEntity.getUsername());
            if(!optional.isPresent()){
                log.info("saveUserService() - Usuário salvo com sucesso - " + userEntity.getUsername());
                return this.userRepository.save(userEntity);
            }else{
                log.info("saveUserService() - Usuário ja cadastrado - " +  userEntity.getUsername());
                throw new Exception("saveUserService() - Username já cadastrado : " + userEntity.getUsername());
            }
        }catch (Exception e){
            log.info("saveUserService() - Internal error when saving user " + e.getMessage());
            throw new RuntimeException("saveUserService() - Internal error when saving user ");
        }
    }
    public ResponseEntity<Object> loginUser(UserRequest user){
       try {
           Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
           // UserEntity userEntity = (UserEntity) authentication.getPrincipal();
           SecurityContextHolder.getContext().setAuthentication(authentication);
           String jwt = jwtUtils.generateJwtToken(authentication);

           return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwt));
       }catch (Exception e){
           log.info("");
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
    }
}
