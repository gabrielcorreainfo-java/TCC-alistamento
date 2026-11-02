package com.example.tcc_alistamento.security.auth;
import org.springframework.security.core.userdetails.UserDetails;

/*
Herda todos os métodos do UserDetails (exigidos pelo Spring Security)
e acrescenta id, nome e perfil de quem está logado.

- O id é usado pelo ControleAcesso para conferir se o usuário é dono dos dados.
- As entidades (Usuario, Medico, Administrador) não precisam implementar
métodos de segurança: isso fica nas classes Details (separação de responsabilidades).
 */

public interface UsuarioAutenticado extends UserDetails {

    Integer getId();

    String getNome();

    // USER, MEDICO ou ADMIN
    String getPerfil();
}
