package com.backend.usuario.domain.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {
    @NotBlank(message = "Campo username não pode ser vazio")
    private String username;
    @NotBlank(message = "Campo password não pode ser vazio")
    private String password;
}
