package com.example.tcc_alistamento.dto;
import com.example.tcc_alistamento.model.Local;

public record LocalResponseDTO(Integer id,
                               String nomeUnidade,
                               String enderecoLocal,
                               String cidadeLocal,
                               String estadoLocal,
                               String cepLocal) {
    public LocalResponseDTO(Local local) {
        this(
                local.getId(),
                local.getNomeUnidade(),
                local.getEnderecoLocal(),
                local.getCidadeLocal(),
                local.getEstadoLocal(),
                local.getCepLocal()
        );
    }

}
