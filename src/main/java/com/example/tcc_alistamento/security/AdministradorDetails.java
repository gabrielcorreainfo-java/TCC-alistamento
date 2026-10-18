package com.example.tcc_alistamento.security;

import com.example.tcc_alistamento.enums.RolesUsers;
import com.example.tcc_alistamento.model.Administrador;
import com.example.tcc_alistamento.model.Medico;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AdministradorDetails implements UsuarioAutenticado {
    private Administrador administrador;

    public AdministradorDetails(Administrador administrador){
        this.administrador = administrador;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(RolesUsers.ADMIN.getRole()));
    }

    @Override
    public String getPassword() {
        // Retorna a senha do administrador (o hash BCrypt guardado no banco).
        return administrador.getSenhaAdmin();
    }

    @Override
    public String getUsername() {
        // Retorna o email usado para fazer login.
        return administrador.getEmailAdmin();
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
        // Informa se o administrador está ativo.
        return true;
    }

    // ----- Dados de quem está logado (interface UsuarioAutenticado) -----

    @Override
    public Integer getId() {
        // id_admin: usado para registrar QUAL administrador analisou o alistamento
        return administrador.getId();
    }

    @Override
    public String getNome() {
        return administrador.getNomeAdmin();
    }

    @Override
    public String getPerfil() {
        return RolesUsers.ADMIN.getRole();
    }
}
