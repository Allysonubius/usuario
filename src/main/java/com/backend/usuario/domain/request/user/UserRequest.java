package com.backend.usuario.domain.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @JsonIgnore
    private UUID id;
    private String username;
    private String password;
    @JsonIgnore
    private String dateCreate;
    @JsonIgnore
    private String dateUpdate;
}
