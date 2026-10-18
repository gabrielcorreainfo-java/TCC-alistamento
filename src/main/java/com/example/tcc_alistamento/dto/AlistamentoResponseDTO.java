package com.example.tcc_alistamento.dto;
import com.example.tcc_alistamento.model.Alistamento;

// Esse DTO representa o que sua API vai devolver quando consultar um alistamento

import java.time.LocalDate;
public record AlistamentoResponseDTO(Integer id,
                                     LocalDate dataAlistamento,
                                     String status,
                                     UsuarioResponseDTO usuarioResponseDTO,
                                     AdministradorResponseDTO administradorResponseDTO) {

    public AlistamentoResponseDTO(Alistamento alistamento) {
        this(
                alistamento.getId(),
                alistamento.getDataAlistamento(),
                alistamento.getStatus(),
                // Passa todo objeto de Usuario para UsuarioResponse
                new UsuarioResponseDTO(alistamento.getUsuario()),
                // O administrador só existe depois da análise, então pode ser nulo
                alistamento.getAdministrador() != null
                        ? new AdministradorResponseDTO(alistamento.getAdministrador())
                        : null
        );
    }
}
