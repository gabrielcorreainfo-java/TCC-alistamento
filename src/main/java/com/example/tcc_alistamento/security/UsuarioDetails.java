package com.example.tcc_alistamento.security;

import com.example.tcc_alistamento.enums.RolesUsers;
import com.example.tcc_alistamento.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// Representa a classe que vai dizer ao Spring Security que ele é Usuário
// Fazendo a ponte entre a entidade e o Spirng Security

public class UsuarioDetails implements UserDetails {
    private final Usuario usuario;

    public UsuarioDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    //Retorna as permissões/roles que o usuário possui para Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // Cria uma lista contendo as permissões do usuário.
        return List.of(

                // Cria uma permissão que o Spring Security consegue entender.
                new SimpleGrantedAuthority(

                        // Pega a role USER do nosso enum.
                        RolesUsers.USER.getRole()
                )
        );
    }

    @Override
    public String getPassword() {
        // Retorna a senha do usuário.
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        // Retorna o email usado para fazer login.
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // Informa se a conta não expirou.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Informa se a conta não está bloqueada.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Informa se as credenciais não expiraram.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Informa se o usuário está ativo.
        return true;
    }
}
