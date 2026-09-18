package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.LocalRequestDTO;
import com.example.tcc_alistamento.dto.LocalResponseDTO;
import com.example.tcc_alistamento.exceptions.LocalNotFoundException;
import com.example.tcc_alistamento.model.Local;
import com.example.tcc_alistamento.repository.LocalRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class LocalService {

    private final LocalRepository localRepository;

    public LocalService(LocalRepository localRepository) {
        this.localRepository = localRepository;
    }

    public LocalResponseDTO salvar(LocalRequestDTO dto) {

        Local local = new Local();

        BeanUtils.copyProperties(dto, local, "id");

        Local localSalvo = localRepository.save(local);

        return new LocalResponseDTO(localSalvo);
    }

    public List<LocalResponseDTO> listar() {

        return localRepository.findAll()
                .stream()
                .map(LocalResponseDTO::new)
                .toList();
    }
    public LocalResponseDTO buscarPorId(Integer id){
        Local localExistente = buscarLocalPorId(id);
        return new LocalResponseDTO(localExistente);

    }
    public Local buscarLocalPorId(Integer id){
        return localRepository.findById(id).orElseThrow(()->new LocalNotFoundException("Local não encontrado"));
    }

    public LocalResponseDTO atualizarLocal(Integer id, LocalRequestDTO dto){
        Local localExistente = buscarLocalPorId(id);
        BeanUtils.copyProperties(dto,localExistente,"id");
        Local localAtualizado = localRepository.save(localExistente);
        return new LocalResponseDTO(localAtualizado);
    }

    public LocalResponseDTO atualizarParcial(Integer id, LocalRequestDTO dto){
        Local localExistente = buscarLocalPorId(id);
        if (dto.nomeUnidade() != null) {
            localExistente.setNomeUnidade(dto.nomeUnidade());
        }

        if (dto.enderecoLocal() != null) {
            localExistente.setEnderecoLocal(dto.enderecoLocal());
        }

        if (dto.cidadeLocal() != null) {
            localExistente.setCidadeLocal(dto.cidadeLocal());
        }

        if (dto.estadoLocal() != null) {
            localExistente.setEstadoLocal(dto.estadoLocal());
        }

        if (dto.cepLocal() != null) {
            localExistente.setCepLocal(dto.cepLocal());
        }

        return new LocalResponseDTO(localRepository.save(localExistente));
    }

    // Nao retorna um DTO
    public void deletarLocal(Integer id){
        Local localExistente = buscarLocalPorId(id);
        localRepository.delete(localExistente);
    }
}
