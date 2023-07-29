package com.backend.usuario.domain.request.dados;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDadosRequest {
    private String nomeCompleto;
    private String dataNascimento;
    private String telefone;
    private String celular;
    private String endereco;
    private String trabalho;
    private String cep;
    private String cpf;
}
