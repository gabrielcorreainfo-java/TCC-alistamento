package com.example.tcc_alistamento.dto;

import com.example.tcc_alistamento.model.Administrador;

import java.util.List;

public record AdministradorAlistamentosResponseDTO(Integer id,
                                                   String nomeAdmin,
                                                   List<AlistamentoResponseDTO> alistamentos) {

    public AdministradorAlistamentosResponseDTO(Administrador administrador) {
        this(
                administrador.getId(),
                administrador.getNomeAdmin(),
                administrador.getAlistamentos()
                        .stream()
                        .map(AlistamentoResponseDTO::new)
                        .toList()
        );
    }

}
