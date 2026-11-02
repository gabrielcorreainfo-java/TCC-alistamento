package com.example.tcc_alistamento.service.integracao;

import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import com.example.tcc_alistamento.repository.AdministradorRepository;
import com.example.tcc_alistamento.repository.MedicoRepository;
import com.example.tcc_alistamento.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

// O login é único para os 3 perfis, então o e-mail não pode se repetir entre eles
@Component
public class VerificadorEmail {
    private final UsuarioRepository usuarioRepository;
    private final AdministradorRepository administradorRepository;
    private final MedicoRepository medicoRepository;

    public VerificadorEmail(UsuarioRepository usuarioRepository,
                            AdministradorRepository administradorRepository,
                            MedicoRepository medicoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.administradorRepository = administradorRepository;
        this.medicoRepository = medicoRepository;
    }

    // Se o e-mail já existe em qualquer um dos 3 perfis: 409
    public void exigirDisponivel(String email) {
        // se der True ele diz como e-mail já cadastrado
        boolean emUso = usuarioRepository.findByEmail(email) != null
                || administradorRepository.findByEmailAdmin(email) != null
                || medicoRepository.findByEmailMedico(email) != null;

        if (emUso) {
            throw new RegraDeNegocioException(HttpStatus.CONFLICT, "Este e-mail já está cadastrado.");
        }
    }
}
