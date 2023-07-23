package com.backend.usuario.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "user_db",name = "user_history")
public class UserHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "history_id", nullable = false)
    @ApiModelProperty(value = "Código de historico do usuário")
    private Long id;
    @ApiModelProperty(value = "Id do usuário")
    @Column(name = "id_user", nullable = false)
    private String idUser;
    @ApiModelProperty(value = "Nome do usuário antigo")
    @Column(name = "old_username", nullable = false)
    private String oldUsername;
    @ApiModelProperty(value = "Nome do usuário novo")
    @Column(name = "new_username", nullable = false)
    private String newUsername;
    @ApiModelProperty(value = "Senha do usuário antiga")
    @Column(name = "old_password", nullable = false)
    private String oldPassword;
    @ApiModelProperty(value = "Senha nova do usuário")
    @Column(name = "new_password", nullable = false)
    private String newPassword;
    @ApiModelProperty(value = "Email do usuario antigo")
    @Column(name = "old_email", nullable = false)
    private String oldEmail;
    @ApiModelProperty(value = "Email do usuario novo")
    @Column(name = "new_email", nullable = false)
    private String newEmail;
    @ApiModelProperty(value = "Perfil do usuario antigo")
    @Column(name = "old_role", nullable = false)
    private String oldRole;
    @ApiModelProperty(value = "Perfil do usuario novo")
    @Column(name = "new_role", nullable = false)
    private String newRole;
}
