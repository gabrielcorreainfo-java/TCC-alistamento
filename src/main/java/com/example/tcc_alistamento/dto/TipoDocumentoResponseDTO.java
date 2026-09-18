package com.example.tcc_alistamento.dto;

import com.example.tcc_alistamento.model.TipoDocumento;

public record TipoDocumentoResponseDTO(Integer idTipoDocumento, String nomeTipo, String descricao) {
    public TipoDocumentoResponseDTO(TipoDocumento tipoDocumento) {
        this( tipoDocumento.getIdTipoDocumento(),
                tipoDocumento.getNomeTipo(),
                tipoDocumento.getDescricao()
        );
    }
}
