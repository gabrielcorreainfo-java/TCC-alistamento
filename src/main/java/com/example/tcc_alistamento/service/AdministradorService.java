package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.response.AdministradorAlistamentosResponseDTO;
import com.example.tcc_alistamento.dto.request.AdministradorRequestDTO;
import com.example.tcc_alistamento.dto.response.AdministradorResponseDTO;
import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import com.example.tcc_alistamento.model.Administrador;
import com.example.tcc_alistamento.repository.AdministradorRepository;
import com.example.tcc_alistamento.service.integracao.VerificadorEmail;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AdministradorService {


    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificadorEmail verificadorEmail;

    public AdministradorService(AdministradorRepository administradorRepository, PasswordEncoder passwordEncoder, VerificadorEmail verificadorEmail) {
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificadorEmail = verificadorEmail;
    }

    public AdministradorResponseDTO salvar(AdministradorRequestDTO dto){
        verificadorEmail.exigirDisponivel(dto.emailAdmin());
        Administrador administrador = new Administrador();
        administrador.setNomeAdmin(dto.nomeAdmin());
        administrador.setEmailAdmin(dto.emailAdmin());
        // A senha precisa ser criptografada, senão o login com BCrypt não funciona
        administrador.setSenhaAdmin(passwordEncoder.encode(dto.senhaAdmin()));
        administradorRepository.save(administrador);

        return new AdministradorResponseDTO(administrador);
    }

    public List<AdministradorResponseDTO> listar(){

        return administradorRepository.findAll()
                .stream().map(AdministradorResponseDTO::new)
                .toList();
    }

    // Método usado para o controller
    public AdministradorResponseDTO buscarPorId(Integer id){

        Administrador administradorExistente = buscarEntidadePorId(id);
        return new AdministradorResponseDTO(administradorExistente);

    }

    // Esse metodo é usado para dentro do service e nao para o controller
    private Administrador buscarEntidadePorId(Integer id){

        return administradorRepository.findById(id)
                .orElseThrow(() ->
                        new RegraDeNegocioException(HttpStatus.NOT_FOUND,
                                "Administrador não encontrado"
                        )
                );
    }

    public AdministradorAlistamentosResponseDTO buscarAlistamentos(Integer id){

        Administrador administrador = buscarEntidadePorId(id);

        return new AdministradorAlistamentosResponseDTO(administrador);
    }

    public AdministradorResponseDTO deletar(Integer id){
        Administrador administrador = buscarEntidadePorId(id);

        administradorRepository.delete(administrador);

        return new AdministradorResponseDTO(administrador);
    }



    public AdministradorResponseDTO atualizar(Integer id, AdministradorRequestDTO dto){

        Administrador administradorExistente = buscarEntidadePorId(id);
        administradorExistente.setNomeAdmin(dto.nomeAdmin());
        administradorExistente.setEmailAdmin(dto.emailAdmin());
        // Só confere se o e-mail mudou
        if (dto.emailAdmin() != null && !dto.emailAdmin().equalsIgnoreCase(administradorExistente.getEmailAdmin())) {
            verificadorEmail.exigirDisponivel(dto.emailAdmin());
        }
        // Só troca a senha se uma nova foi enviada
        if (dto.senhaAdmin() != null && !dto.senhaAdmin().isBlank()) {
            administradorExistente.setSenhaAdmin(passwordEncoder.encode(dto.senhaAdmin()));
        }
        administradorRepository.save(administradorExistente);
        return new AdministradorResponseDTO(administradorExistente);
    }

    public AdministradorResponseDTO atualizarParcial(Integer id, AdministradorRequestDTO dto){

        Administrador adminExistente = buscarEntidadePorId(id);


        if (dto.nomeAdmin()!= null) {
            adminExistente.setNomeAdmin(dto.nomeAdmin());
        }

        // Só confere e troca se veio um e-mail diferente
        if (dto.emailAdmin() != null && !dto.emailAdmin().equalsIgnoreCase(adminExistente.getEmailAdmin())) {
            verificadorEmail.exigirDisponivel(dto.emailAdmin());
            adminExistente.setEmailAdmin(dto.emailAdmin());
        }

        if (dto.senhaAdmin() != null && !dto.senhaAdmin().isBlank()) {
            adminExistente.setSenhaAdmin(passwordEncoder.encode(dto.senhaAdmin()));
        }

        administradorRepository.save(adminExistente);

        return new AdministradorResponseDTO(adminExistente);
    }
}

