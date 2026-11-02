package com.example.tcc_alistamento.security;

import com.example.tcc_alistamento.enums.RolesUsers;
import com.example.tcc_alistamento.model.Medico;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class MedicoDetails implements UsuarioAutenticado {

    private Medico medico;

    public MedicoDetails(Medico medico){
        this.medico=medico;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority(RolesUsers.MEDICO.getRole()));
    }
    @Override
    public String getPassword() {
        return medico.getSenhaMedico();
    }

    @Override
    public String getUsername() {
        return medico.getEmailMedico();
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
        return medico.getId();
    }

    @Override
    public String getNome() {
        return medico.getNomeMedico();
    }

    @Override
    public String getPerfil() {
        return RolesUsers.MEDICO.getRole();
    }
}
