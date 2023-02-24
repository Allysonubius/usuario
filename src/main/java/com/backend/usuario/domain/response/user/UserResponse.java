package com.backend.usuario.domain.response.user;

import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String username;
    private String dateCreate;
    private String dateUpdate;
}
