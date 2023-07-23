package com.backend.usuario.domain.mapper;

import com.backend.usuario.domain.request.role.RoleUserRequest;
import com.backend.usuario.domain.request.user.UserCreateUserRequest;
import com.backend.usuario.domain.response.user.UserResponse;
import com.backend.usuario.repository.entity.UserEntity;
import com.backend.usuario.exception.UserServiceException;
import com.backend.usuario.service.UserService;
import com.backend.usuario.util.EmailValidatorUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.backend.usuario.domain.constants.SecurityConstants.ACTIVE_ACCOUNT;

@Slf4j
@Component
@AllArgsConstructor
public class UserMapper {
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserService userService;
    /**
     * @param userEntity
     * @return
     */
    public UserResponse toUserResponse(UserEntity userEntity){
        return this.modelMapper.map(userEntity, UserResponse.class);
    }
    /**
     * @param userCreateUserRequest
     * @return
     */
    public UserEntity toUserRequest(UserCreateUserRequest userCreateUserRequest){
        if(userCreateUserRequest.getUsername().isEmpty() || userCreateUserRequest.getPassword().isEmpty()){
            log.info("toUserRequest() - Empty username or password in user: {}", userCreateUserRequest);
            throw new UserServiceException("Empty username or password in user: " + userCreateUserRequest.getUsername());
        }
        userCreateUserRequest.setId(UUID.randomUUID());
        userCreateUserRequest.setUsername(userCreateUserRequest.getUsername());
        userCreateUserRequest.setPassword(passwordEnconde(userCreateUserRequest.getPassword()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3")); // Definido GMT-3 como horário de Brasília
        userCreateUserRequest.setDateCreate(dateFormat.format(new Date()));
        userCreateUserRequest.setDateUpdate(null);

        EmailValidatorUtil validator = new EmailValidatorUtil();
        if (!validator.validate(userCreateUserRequest.getEmail())) {
            log.info("toUserRequest() - Inválido email:{} ", userCreateUserRequest.getEmail());
            throw new UserServiceException("Inválido email: " + userCreateUserRequest.getEmail());
        }
        userCreateUserRequest.setEmail(userCreateUserRequest.getEmail());

        RoleUserRequest roleUserRequest = userService.getRoleById(userCreateUserRequest.getRole().getId());
        if (roleUserRequest == null) {
            throw new UserServiceException("Role com ID: " + userCreateUserRequest.getRole().getId() + " não encontrado.");
        }
        roleUserRequest.setId(userCreateUserRequest.getRole().getId());
        userCreateUserRequest.setRole(roleUserRequest);

        userCreateUserRequest.setActive(ACTIVE_ACCOUNT);

        return this.modelMapper.map(userCreateUserRequest, UserEntity.class);
    }
    /**
     * @param password
     * @return
     */
    private String passwordEnconde(String password){
        return passwordEncoder.encode(password);
    }
}
