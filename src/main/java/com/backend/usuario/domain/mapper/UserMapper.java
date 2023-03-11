package com.backend.usuario.domain.mapper;

import com.backend.usuario.domain.request.user.UserRequest;
import com.backend.usuario.domain.response.user.UserResponse;
import com.backend.usuario.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class UserMapper {
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    public UserEntity toUserRequest(UserRequest userRequest){
        try {
            userRequest.setId(UUID.randomUUID());
            if(userRequest.getUsername().isEmpty()){
                userRequest.setUsername(userRequest.getUsername());
            }
            if(userRequest.getPassword().isEmpty()){
                String formatPassword = this.passwordEncoder.encode(userRequest.getPassword());
                userRequest.setPassword(formatPassword);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            userRequest.setDateCreate(dateFormat.format(new Date()));
            userRequest.setDateUpdate(null);
        }catch (Exception e){
            log.info("toUserRequest() - ");
            throw new RuntimeException("toUserRequest() - " + e.getMessage());
        }
        return this.modelMapper.map(userRequest, UserEntity.class);
    }
    public UserResponse toUserResponse(UserEntity userEntity){
        return this.modelMapper.map(userEntity, UserResponse.class);
    }
}
