package com.example.tcc_alistamento.dto;

import java.time.LocalDate;

public record UsuarioRequestDTO( String nome,
                                 LocalDate dataNascimento,
                                 String email,
                                 String senha,
                                 String telefone,
                                 String cpf,
                                 String nomePai,
                                 String nomeMae,
                                 String estadoCivil,
                                 String uf,
                                 String escolaridade,
                                 String rg,
                                 String localNascimento,
                                 String cep,
                                 String bairro,
                                 String municipio,
                                 String paisResidencia,
                                 String zonaResidencial,
                                 String numeroResidencia,
                                 String logradouro,
                                 String estado) {
}
