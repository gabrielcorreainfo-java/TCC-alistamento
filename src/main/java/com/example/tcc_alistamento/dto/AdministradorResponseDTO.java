package com.example.tcc_alistamento.dto;
import com.example.tcc_alistamento.entity.Administrador;
// Esse DTO representa os dados do administrador que você quer devolver na resposta.

public record AdministradorResponseDTO(Integer id,
                                       String nomeAdmin,
                                       String emailAdmin) {

    // Trasforma a Entity em um DTO
    public AdministradorResponseDTO(Administrador administrador) {
        this(
                administrador.getId(),
                administrador.getNomeAdmin(),
                administrador.getEmailAdmin()
        );
    }
}
