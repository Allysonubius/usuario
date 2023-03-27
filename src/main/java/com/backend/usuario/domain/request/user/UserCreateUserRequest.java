package com.backend.usuario.domain.request.user;

import com.backend.usuario.domain.request.role.RoleUserRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.List;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateUserRequest {
    @JsonIgnore
    private UUID id;
    @NotBlank(message = "Campo username não pode ser vazio")
    private String username;
    @NotBlank(message = "Campo password não pode ser vazio")
    private String password;
    @JsonIgnore
    private String dateCreate;
    @JsonIgnore
    private String dateUpdate;
    @NotBlank(message = "Campo email não pode ser vazio")
    private String email;

    private RoleUserRequest role ;

}
