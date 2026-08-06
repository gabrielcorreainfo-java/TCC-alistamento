package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.AdministradorRequestDTO;
import com.example.tcc_alistamento.dto.AdministradorResponseDTO;
import com.example.tcc_alistamento.dto.AlistamentoResponseDTO;
import com.example.tcc_alistamento.entity.Administrador;
import com.example.tcc_alistamento.exceptions.AdministradorNotFoundException;
import com.example.tcc_alistamento.repository.AdministradorRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class AdministradorService {


    private final AdministradorRepository administradorRepository;

    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public AdministradorResponseDTO salvar(AdministradorRequestDTO dto){

        Administrador administrador = new Administrador();
        administrador.setEmailAdmin(dto.emailAdmin());
        administrador.setNomeAdmin(dto.nomeAdmin());
        administrador.setSenhaAdmin(dto.senhaAdmin());

        Administrador administradorSalvo = administradorRepository.save(administrador);
        return new AdministradorResponseDTO(administrador);

    }

    public List<AdministradorResponseDTO> listar(){

        return administradorRepository.findAll()
                .stream().map(AdministradorResponseDTO::new)
                .toList();
    }

    public AdministradorResponseDTO buscarPorId(Integer id){
        Administrador administradorExistente = administradorRepository.findById(id).orElseThrow(() ->
                new AdministradorNotFoundException("Administrador não encontrado"));
        return new AdministradorResponseDTO(administradorExistente);

    }

    public AdministradorResponseDTO deletar(Integer id){

        AdministradorResponseDTO admin = buscarPorId(id);
        administradorRepository.save(admin)
        return new AdministradorResponseDTO(admin);
    }

    public Administrador login(String email, String senha){
        /*
        Cria uma variável chamada admin do tipo Administrador e
        coloca dentro dela o objeto que veio do banco através do método findByEmailAdmin(email).
         */
        Administrador admin = administradorRepository.findByEmailAdmin(email);

        if( admin != null && admin.getSenhaAdmin().equals(senha)){
            return admin;
        }

        return null;
    }

    public Administrador atualizar(Integer id, Administrador administrador){

        Administrador administradorExistente = buscarPorId(id);

        BeanUtils.copyProperties(administrador,administradorExistente,"id");
        return administradorRepository.save(administradorExistente);

    }

    public Administrador atualizarParcial(Integer id, Administrador administrador){

        Administrador adminExistente = buscarPorId(id);

        if (administrador.getNomeAdmin() != null) {
            adminExistente.setNomeAdmin(administrador.getNomeAdmin());
        }

        if (administrador.getEmailAdmin() != null) {
            adminExistente.setEmailAdmin(administrador.getEmailAdmin());
        }

        if (administrador.getSenhaAdmin() != null) {
            adminExistente.setSenhaAdmin(administrador.getSenhaAdmin());
        }

        return administradorRepository.save(adminExistente);
    }

    public Administrador deletarAdministrador(Integer id){
        Administrador administradorExistente = buscarPorId(id);
         administradorRepository.deleteById(id);
        return administradorExistente;
    }
}

