package com.example.tcc_alistamento.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioAutenticado extends UserDetails {
    // id na tabela do perfil (id_usuario, id_medico ou id_admin)
    Integer getId();

    String getNome();

    // USER, MEDICO ou ADMIN
    String getPerfil();
}
