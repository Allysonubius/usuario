package com.backend.usuario.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "user_db",name = "user_tb")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 2405172041950251807L;
    @ApiModelProperty(value = "Código da pessoa")
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user", nullable = false, updatable = false, unique = true, columnDefinition = "VARCHAR(55)")
    protected UUID id;
    @ApiModelProperty(value = "Nome da pessoa")
    @Column(name = "username", nullable = false)
    private String username;
    @ApiModelProperty(value = "Senha da pessoa")
    @Column(name = "password", nullable = false)
    private String password;
    @ApiModelProperty(value = "Data de criação")
    @Column(name="date_create", nullable = false)
    private Date dateCreate;
    @ApiModelProperty(value = "Data de atualização")
    @Column(name="date_update", nullable = false)
    private Date dateUpdate;
    @ApiModelProperty(value = "Email da pessoa")
    @Column(name = "email", nullable = false)
    private String email;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_role", referencedColumnName = "role_id")
    private UserRoleEntity role;
}