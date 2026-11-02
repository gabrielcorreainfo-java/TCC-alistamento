package com.example.tcc_alistamento.dto.request;

// O que chega da tela de redefinição: e-mail, código recebido e senha nova
public record RedefinirSenhaDTO(String email,
                                String codigo,
                                String novaSenha) {
}
