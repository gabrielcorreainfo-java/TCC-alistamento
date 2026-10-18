package com.example.tcc_alistamento.dto;

public record EnderecoResponseDTO(String cep,
                                  String logradouro,
                                  String bairro,
                                  String municipio,
                                  String uf) {
}
