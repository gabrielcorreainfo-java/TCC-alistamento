package com.example.tcc_alistamento.service;

// Lógica da autenticação do Usuário

import com.example.tcc_alistamento.model.Administrador;
import com.example.tcc_alistamento.model.Medico;
import com.example.tcc_alistamento.model.Usuario;
import com.example.tcc_alistamento.repository.AdministradorRepository;
import com.example.tcc_alistamento.repository.MedicoRepository;
import com.example.tcc_alistamento.repository.UsuarioRepository;
import com.example.tcc_alistamento.security.AdministradorDetails;
import com.example.tcc_alistamento.security.MedicoDetails;
import com.example.tcc_alistamento.security.UsuarioDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    // UsuarioRepository para poder se comunicar com o BD
    UsuarioRepository usuarioRepository;

    @Autowired
    AdministradorRepository administradorRepository;

    @Autowired
    MedicoRepository medicoRepository;

    @Override
    // Alguém tentar se autentica, essa forma o Spring Security consulta esse pessoa
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username);
        if (usuario != null) {
            return new UsuarioDetails(usuario);
        }

        Administrador administrador = administradorRepository.findByEmailAdmin(username);
        if (administrador != null) {
            return new AdministradorDetails(administrador);
        }

        Medico medico = medicoRepository.findByEmailMedico(username);
        if (medico != null) {
            return new MedicoDetails(medico);
        }

        throw new UsernameNotFoundException("Usuário não encontrado com esse e-mail");
    }
}
