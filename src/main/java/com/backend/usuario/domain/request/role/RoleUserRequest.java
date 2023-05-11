package com.backend.usuario.domain.request.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleUserRequest {
    @Size(min = 5, max = 5, message = "O campo role_id deve ser preenchido !")
    private Long id;
}
