package com.backend.usuario.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "user_db",name = "user_creation_history")
public class UserCreationHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "history_id", nullable = false)
    @ApiModelProperty(value = "Código de historico do usuário")
    private Long id;
    @ApiModelProperty(value = "Id do usuário")
    @Column(name = "id_user", nullable = false)
    private String idUser;
    @ApiModelProperty(value = "Nome do usuário")
    @Column(name = "username", nullable = false)
    private String username;
    @ApiModelProperty(value = "Senha do usuário")
    @Column(name = "password", nullable = false)
    private String password;
    @ApiModelProperty(value = "Email do usuário")
    @Column(name = "email", nullable = false)
    private String email;
    @ApiModelProperty(value = "Perfil de acesso")
    @Column(name = "user_role", nullable = false)
    private Long userRole;
    @ApiModelProperty(value = "Data de criação")
    @Column(name = "date_created", nullable = false)
    private Date dateCreated;
}
