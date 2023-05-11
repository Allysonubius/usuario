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
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@ControllerAdvice
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    /**
     * @param userEntity
     * @return
     */
    public UserEntity saveUserService(UserEntity userEntity){
        try{
            log.info("loginUser() - Starting saving new user - user:[{}]", userEntity.toString());
            Optional<UserEntity> optionalUsername = this.userRepository.findByUsername(userEntity.getUsername());
            checkIfUsernameAlreadyExists(optionalUsername, userEntity);

            Optional<UserEntity> optionalEmail = this.userRepository.findByEmail(userEntity.getEmail());
            checkIfEmailAlreadyExists(optionalEmail, userEntity);

            UserEntity savedUser = this.userRepository.save(userEntity);
            log.info("saveUserService() - User saved successfully - user: {}", savedUser.toString());
            return savedUser;
        } catch (UserServiceException e) {
            log.info("saveUserService() - Error when saving user - message: {}", e.getMessage());
            throw new UserServiceException("Error when saving user: " + e.getMessage());
        } catch (Exception e) {
            log.error("saveUserService() - Unknown error when saving user - message: {}", e.getMessage());
            throw new UserServiceException("Unknown error when saving user: " + e.getMessage());
        }
    }
    /**
     * @param user
     * @return
     */
    public ResponseEntity<Object> loginUser(@Valid UserLoginRequest user) {
        try {
            log.info("loginUser() - Starting user login - username: {}", user.getUsername());
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken(user.getUsername(), user.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication, user);

            log.info("loginUser() - User login successful - username: {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwt));
        }catch (JwtException e){
            log.error("loginUser() - Error creating JWT token: {}", e.getMessage());
            throw new UserServiceException("Error creating JWT token .");
        }catch (UserServiceException e) {
            log.info("loginUser() - Error during user login - message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Error during user login.", e.getLocalizedMessage(), LocalDateTime.now()));
        }
    }
    /**
     * @param id
     */
    public void deleteUser(UUID id){
        try {
            log.info("deleteUser() - Starting to delete user - ID: {}", id);
            deleteUserSearch(id);
            this.userRepository.deleteById(id);
        }catch (UserServiceException e){
            log.info("deleteUser() - Failed to delete user ID:{}. Reason: {}", id, e.getMessage());
            throw new UserServiceException("Failed to delete user ID: " + id);
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
     * @param id
     * @return
     */
    public RoleUserRequest getRoleById(Long id) {
        log.info("getRoleById() - Starting get role by id - id:{}", id);
        UserRoleEntity userRole = getUserRoleById(id).get();
        RoleUserRequest roleUser = new RoleUserRequest();
        roleUser.setId(userRole.getId());
        log.info("getRoleById() - Role found - id:{}", id);
        return roleUser;
    }
    /**
     * @param id
     */
    public void deleteUserSearch(UUID id) {
        Optional<UserEntity> userOpt = this.userRepository.findById(id);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            this.userRepository.delete(user);
            log.info("deleteUserSearch() - User deleted successfully - ID: {}", id);
        } else {
            log.error("deleteUserSearch() - User not found for ID:{}", id);
            throw new UserServiceException("User not found for ID: " + id);
        }
    }
    /**
     * @param optionalUsername
     * @param userEntity
     */
    private void checkIfUsernameAlreadyExists(Optional<UserEntity> optionalUsername, UserEntity userEntity) {
        if(optionalUsername.isPresent()){
            log.info("saveUserService() - Username already registered - username:{}", userEntity.getUsername());
            throw new UserServiceException("Username already registered - username: " + userEntity.getUsername());
        }
    }

    /**
     * @param optionalEmail
     * @param userEntity
     */
    private void checkIfEmailAlreadyExists(Optional<UserEntity> optionalEmail, UserEntity userEntity) {
        if(optionalEmail.isPresent()){
            log.info("saveUserService() - Email already registered - email:{}", userEntity.getEmail());
            throw new UserServiceException("Email already registered - email: " + userEntity.getEmail());
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
    /**
     * @param id
     * @return
     */
    private Optional<UserRoleEntity> getUserRoleById(Long id) {
        log.info("getUserRoleById() - Retrieving role by ID:{}", id);
        Optional<UserRoleEntity> userRoleOpt = this.userRoleRepository.findById(id);
        if(userRoleOpt.isPresent()){
            log.info("getUserRoleById() - Role found for ID:{}", id);
            return userRoleOpt;
        }else {
            log.error("getRoleById() - Role not found for ID:{}", id);
            throw new UserServiceException("Role not found for ID: " + id);
        }
    }
}
