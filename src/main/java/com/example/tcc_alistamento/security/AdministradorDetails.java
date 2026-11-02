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
        return administrador.getSenhaAdmin();
    }

    @Override
    public String getUsername() {
        return administrador.getEmailAdmin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Integer getId() {
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
