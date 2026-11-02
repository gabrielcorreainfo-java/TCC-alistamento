package com.example.tcc_alistamento.dto.request;

public record LocalRequestDTO(String nomeUnidade,
                              String enderecoLocal,
                              String cidadeLocal,
                              String estadoLocal,
                              String cepLocal) {
}
