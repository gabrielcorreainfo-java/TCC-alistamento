package com.example.tcc_alistamento.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DocumentoRequestDTO(String numeroDocumento,
                                  String numeroFolha,
                                  String numeroLivro,
                                  LocalDate dataEmissao,
                                  String orgaoEmissor,
                                  String cidadeEmissao,
                                  String estadoEmissao,
                                  String nomeArquivo,
                                  LocalDateTime dataEnvio,
                                  Integer idUsuario,
                                  Integer idAlistamento,
                                  Integer idTipoDocumento) {
}
