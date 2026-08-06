package com.example.tcc_alistamento.dto;

import java.time.LocalDate;

public record UsuarioRequestDTO(Integer id,
                                String nome,
                                LocalDate dataNascimento,
                                String email,
                                String telefone,
                                String cpf) {
}
