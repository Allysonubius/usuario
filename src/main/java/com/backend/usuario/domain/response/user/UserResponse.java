package com.backend.usuario.domain.response.user;

import com.backend.usuario.domain.response.role.RoleResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private String id;
    private String username;
    private String dateCreate;
    private String dateUpdate;
    private String email;
    private Set<RoleResponse> role;
    private String active;
}
