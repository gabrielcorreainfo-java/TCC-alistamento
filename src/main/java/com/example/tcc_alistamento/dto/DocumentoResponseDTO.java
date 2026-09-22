package com.example.tcc_alistamento.dto;

import com.example.tcc_alistamento.model.Documento;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DocumentoResponseDTO(Integer id,
                                   String numeroDocumento,
                                   String numeroFolha,
                                   String numeroLivro,
                                   LocalDate dataEmissao,
                                   String orgaoEmissor,
                                   String cidadeEmissao,
                                   String estadoEmissao,
                                   String nomeArquivo,
                                   LocalDateTime dataEnvio,
                                   String status,
                                   UsuarioResponseDTO usuarioResponseDTO,
                                   AlistamentoResponseDTO alistamentoResponseDTO,
                                   TipoDocumentoResponseDTO tipoDocumentoResponseDTO) {
    public DocumentoResponseDTO(Documento documento) {
        this(
                documento.getIdDocumento(),
                documento.getNumeroDocumento(),
                documento.getNumeroFolha(),
                documento.getNumeroLivro(),
                documento.getDataEmissao(),
                documento.getOrgaoEmissor(),
                documento.getCidadeEmissao(),
                documento.getEstadoEmissao(),
                documento.getNomeArquivo(),
                documento.getDataEnvio(),
                documento.getStatus(),
                new UsuarioResponseDTO(documento.getUsuario()),
                new AlistamentoResponseDTO(documento.getAlistamento()),
                new TipoDocumentoResponseDTO(documento.getTipoDocumento())
        );
    }
}
