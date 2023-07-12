package com.backend.usuario.service;

import com.backend.usuario.config.data.jwt.JwtUtils;
import com.backend.usuario.domain.request.user.UserLoginRequest;
import com.backend.usuario.domain.response.erro.ErrorResponse;
import com.backend.usuario.domain.response.jwt.JwtResponse;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.backend.usuario.domain.constants.SecurityConstants.ACTIVE_ACCOUNT;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@ControllerAdvice
public class LoginService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    /**
     * @param user
     * @return
     */
    public ResponseEntity<Object> loginUser(@Valid UserLoginRequest user) {
        try {
            log.info("loginUser() - Starting user login - username: {}", user.getUsername());
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken(user.getUsername(), user.getPassword()));
            UserEntity userEntity = getUserByUsername(user.getUsername());
            if (userEntity != null && userEntity.getActive() != null &&  userEntity.getActive().contains("true")){
                SecurityContext securityContext = SecurityContextHolder.getContext();
                // Verificar se o usuário está autenticado
                if(securityContext.getAuthentication() != null && securityContext.getAuthentication().isAuthenticated()){

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String jwt = jwtUtils.generateJwtToken(authentication, user);

                    log.info("loginUser() - User login successful - username: {}", user.getUsername());
                    return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwt));
                }else {
                    log.error("loginUser() - User not authenticated.");
                    throw new UserServiceException("loginUser() - User not authenticated.");
                }
            }else {
                log.error("loginUser() - Inactive or non-existent user account.");
                throw new UserServiceException("loginUser() - Inactive or non-existent user account.");
            }
        }catch (UserServiceException e) {
            log.info("loginUser() - Error during user login - message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Error during user login.", e.getLocalizedMessage(), LocalDateTime.now()));
        }
    }
    /**
     * @param username
     * @return
     */
    public String refresh(String username) {
        log.info("refresh() - Starting token refresh - username: [{}]", username);
        if (username.isEmpty()) {
            log.error("refresh() - Username is empty");
            throw new UserServiceException("Username is empty");
        }
        return jwtUtils.createToken(username,this.userRepository.findByUsername(username));
    }
    /**
     * @param username
     * @param password
     * @return
     */
    private UsernamePasswordAuthenticationToken authenticationToken(String username, String password){
        return new UsernamePasswordAuthenticationToken(username,password);
    }

    /**
     * @param username
     * @return
     */
    private UserEntity getUserByUsername(String username) {
        Optional<UserEntity> userOptional = userRepository.findByUsername(username);
        return userOptional.orElse(null);
    }
}