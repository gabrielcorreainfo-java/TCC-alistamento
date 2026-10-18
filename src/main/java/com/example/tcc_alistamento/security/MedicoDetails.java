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
        // Retorna a senha do médico (o hash BCrypt guardado no banco).
        return medico.getSenhaMedico();
    }

    @Override
    public String getUsername() {
        // Retorna o email usado para fazer login.
        return medico.getEmailMedico();
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
        // Informa se o médico está ativo.
        return true;
    }

    // ----- Dados de quem está logado (interface UsuarioAutenticado) -----

    @Override
    public Integer getId() {
        // id_medico: usado para saber QUAL médico registrou a avaliação
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
