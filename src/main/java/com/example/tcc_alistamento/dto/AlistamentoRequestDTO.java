package com.example.tcc_alistamento.dto;
import java.time.LocalDate;
// Esse DTO Dados que entram na API como um PUT ou POST

public record AlistamentoRequestDTO(LocalDate dataAlistamento,
                                    String status,
                                    Integer idUsuario,
                                    Integer idAdministrador) {
}
