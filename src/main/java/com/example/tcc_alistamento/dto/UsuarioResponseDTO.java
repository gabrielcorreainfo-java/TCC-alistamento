package com.example.tcc_alistamento.dto;

import com.example.tcc_alistamento.model.Usuario;

import java.time.LocalDate;

public record UsuarioResponseDTO(Integer id,
                                 String nome,
                                 LocalDate dataNascimento,
                                 String email,
                                 String telefone,
                                 String cpf) {

    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getDataNascimento(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getCpf()
        );
    }
}
