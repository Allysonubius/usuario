package com.backend.usuario.domain.mapper;

import com.backend.usuario.domain.request.user.UserRequest;
import com.backend.usuario.domain.response.user.UserResponse;
import com.backend.usuario.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
@AllArgsConstructor
public class UserMapper {
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    public UserEntity toUserRequest(UserRequest userRequest){
        userRequest.setId(UUID.randomUUID());
        if(userRequest.getUsername().isEmpty() || userRequest.getUsername() != null){
            userRequest.setUsername(userRequest.getUsername());
        }
        if(userRequest.getPassword().isEmpty() || userRequest.getPassword() != null){
            userRequest.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        userRequest.setDateCreate(dateFormat.format(new Date()));
        userRequest.setDateUpdate(null);
        return this.modelMapper.map(userRequest, UserEntity.class);
    }
    public UserResponse toUserResponse(UserEntity userEntity){
        return this.modelMapper.map(userEntity, UserResponse.class);
    }
}
