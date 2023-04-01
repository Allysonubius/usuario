package com.backend.usuario.domain.mapper;

import com.backend.usuario.domain.request.role.RoleUserRequest;
import com.backend.usuario.domain.request.user.UserCreateUserRequest;
import com.backend.usuario.domain.response.user.UserResponse;
import com.backend.usuario.entity.UserEntity;
import com.backend.usuario.entity.UserRoleEntity;
import com.backend.usuario.service.UserService;
import com.backend.usuario.util.EmailValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;


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
        try {
            if(userCreateUserRequest.getUsername().isEmpty() || userCreateUserRequest.getPassword().isEmpty()){
                log.info("toUserRequest() - ");
                throw new RuntimeException("toUserRequest() - " + userCreateUserRequest.getPassword() + userCreateUserRequest.getUsername());
            }
            userCreateUserRequest.setId(UUID.randomUUID());
            userCreateUserRequest.setUsername(userCreateUserRequest.getUsername());
            userCreateUserRequest.setPassword(passwordEncoder.encode(userCreateUserRequest.getPassword()));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            userCreateUserRequest.setDateCreate(dateFormat.format(new Date()));
            userCreateUserRequest.setDateUpdate(null);

            EmailValidator validator = new EmailValidator();
            if (!validator.validate(userCreateUserRequest.getEmail())) {
                log.info("toUserRequest() - O email e inválido - email:[{}] ", userCreateUserRequest.getEmail());
                throw new RuntimeException("toUserRequest() - O email e inválido - " + userCreateUserRequest.getEmail());
            }
            userCreateUserRequest.setEmail(userCreateUserRequest.getEmail());

            RoleUserRequest roleUserRequest = userService.getRoleById(userCreateUserRequest.getRole().getId());
            if (roleUserRequest == null) {
                throw new RuntimeException("toUserRequest() - Role com ID " + userCreateUserRequest.getRole() + " não encontrada.");
            }
            roleUserRequest.setId(userCreateUserRequest.getRole().getId());
            userCreateUserRequest.setRole(roleUserRequest);

            return this.modelMapper.map(userCreateUserRequest, UserEntity.class);

        } catch (Exception e){
            log.info("toUserRequest() - ");
            throw new RuntimeException("toUserRequest() - " + e.getMessage());
        }
    }
}
