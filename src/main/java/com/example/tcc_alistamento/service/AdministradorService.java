package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.AdministradorAlistamentosResponseDTO;
import com.example.tcc_alistamento.dto.AdministradorRequestDTO;
import com.example.tcc_alistamento.dto.AdministradorResponseDTO;
import com.example.tcc_alistamento.model.Administrador;
import com.example.tcc_alistamento.exceptions.AdministradorNotFoundException;
import com.example.tcc_alistamento.repository.AdministradorRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@Service
public class AdministradorService {


    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    public AdministradorService(AdministradorRepository administradorRepository, PasswordEncoder passwordEncoder) {
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AdministradorResponseDTO salvar(AdministradorRequestDTO dto){

        Administrador administrador = new Administrador();

        BeanUtils.copyProperties(dto, administrador,"id");

        administradorRepository.save(administrador);

        return new AdministradorResponseDTO(administrador);
    }

    // login() removido — a autenticação agora passa pelo authenticationManager
    // no AdministradorController, igual já é feito para Usuario.

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
                        new AdministradorNotFoundException(
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

        BeanUtils.copyProperties(dto,administradorExistente,"id");

        administradorRepository.save(administradorExistente);

        return new AdministradorResponseDTO(administradorExistente);

    }

    public AdministradorResponseDTO atualizarParcial(Integer id, AdministradorRequestDTO dto){

        Administrador adminExistente = buscarEntidadePorId(id);


        if (dto.nomeAdmin()!= null) {
            adminExistente.setNomeAdmin(dto.nomeAdmin());
        }

        if (dto.emailAdmin() != null) {
            adminExistente.setEmailAdmin(dto.emailAdmin());
        }

        if (dto.senhaAdmin() != null) {
            adminExistente.setSenhaAdmin(dto.senhaAdmin());
        }
        administradorRepository.save(adminExistente);

        return new AdministradorResponseDTO(adminExistente);
    }
}

