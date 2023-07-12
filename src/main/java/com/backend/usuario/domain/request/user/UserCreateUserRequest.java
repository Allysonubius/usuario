package com.backend.usuario.domain.request.user;

import com.backend.usuario.domain.request.role.RoleUserRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateUserRequest {
    @JsonIgnore
    private UUID id;
    @NotBlank(message = "Campo username não pode ser vazio")
    @Size(min = 6, max = 40, message = "O campo username deve ter entre 6 e 40 caracteres")
    private String username;
    @NotBlank(message = "Campo password não pode ser vazio")
    @Size(min = 8, max = 25, message = "O campo email deve ter entre 8 e 25 caracteres")
    private String password;
    @JsonIgnore
    private String dateCreate;
    @JsonIgnore
    private String dateUpdate;
    @NotBlank(message = "Campo email não pode ser vazio")
    @Size(min = 6, max = 40, message = "O campo email deve ter entre 6 e 40 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email inválido")
    private String email;
    private RoleUserRequest role ;
    @JsonIgnore
    private String active;
}
