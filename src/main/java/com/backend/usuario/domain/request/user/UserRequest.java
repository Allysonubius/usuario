package com.backend.usuario.domain.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
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
}
