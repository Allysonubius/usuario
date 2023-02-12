package com.backend.usuario.domain.request.user;

import com.backend.usuario.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private UUID id;
    private String username;
    private String password;
}
