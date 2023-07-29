package com.backend.usuario.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "user_db", name = "user_data")
public class UserDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "dados_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "data_nascimento")
    private String dataNascimento;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "celular")
    private String celular;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "trabalho")
    private String trabalho;

    @Column(name = "cep")
    private String cep;

    @Column(name = "cpf")
    private String cpf;
}
