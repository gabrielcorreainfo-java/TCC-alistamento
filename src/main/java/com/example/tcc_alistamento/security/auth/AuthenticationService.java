package com.example.tcc_alistamento.security.auth;

import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import com.example.tcc_alistamento.model.Administrador;
import com.example.tcc_alistamento.model.Medico;
import com.example.tcc_alistamento.model.Usuario;
import com.example.tcc_alistamento.repository.AdministradorRepository;
import com.example.tcc_alistamento.repository.MedicoRepository;
import com.example.tcc_alistamento.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
  - Classe responsável lógica da autenticação
  - Implementa UserDetailsService para o Spring Security
    identificar da classe ser um Service
 */
@Service
public class AuthenticationService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final AdministradorRepository administradorRepository;
    private final MedicoRepository medicoRepository;

    public AuthenticationService(UsuarioRepository usuarioRepository,
                                 AdministradorRepository administradorRepository,
                                 MedicoRepository medicoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.administradorRepository = administradorRepository;
        this.medicoRepository = medicoRepository;
    }

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

        throw new RegraDeNegocioException(HttpStatus.NOT_FOUND,"Usuário não encontrado com esse e-mail");
    }
}
