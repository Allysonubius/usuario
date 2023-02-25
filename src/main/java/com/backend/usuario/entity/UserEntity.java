package com.backend.usuario.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "user_db",name = "user_tb")
public class UserEntity {
    private static final long serialVersionUID = -6495853850202569574L;
    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user", nullable = false, updatable = false, unique = true, columnDefinition = "VARCHAR(55)")
    protected UUID id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name="date_create", nullable = false)
    private Date dateCreate;
    @Column(name="date_update", nullable = false)
    private Timestamp dateUpdate;
}
