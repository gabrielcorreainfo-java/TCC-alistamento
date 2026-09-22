package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.MedicoRequestDTO;
import com.example.tcc_alistamento.dto.MedicoResponseDTO;
import com.example.tcc_alistamento.exceptions.MedicoNotFoundException;
import com.example.tcc_alistamento.model.Medico;
import com.example.tcc_alistamento.repository.MedicoRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class MedicoService {
    private final MedicoRepository medicoRepository;

    // Injeção de dependência da MedicoRepository.
    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    // Cadastrar médico.
    public MedicoResponseDTO salvar(MedicoRequestDTO dto) {

        Medico medico = new Medico();

        medico.setNomeMedico(dto.nomeMedico());
        medico.setCrm(dto.crm());
        medico.setEspecialidade(dto.especialidade());
        medico.setTelefoneMedico(dto.telefoneMedico());
        medico.setEmailMedico(dto.emailMedico());
        medico.setSenhaMedico(dto.senhaMedico());
        return new MedicoResponseDTO(medicoRepository.save(medico));
    }

    // login() removido — a autenticação agora passa pelo authenticationManager
    // no MedicoController, igual já é feito para Usuario.

    // Listar todos os médicos.
    public List<MedicoResponseDTO> listar() {

        return medicoRepository.findAll()
                .stream()
                .map(MedicoResponseDTO::new)
                .toList();
    }

    // Buscar médico pelo ID.
    public MedicoResponseDTO buscarPorId(Integer id) {

        Medico medico = buscarMedico(id);

        return new MedicoResponseDTO(medico);
    }

    // Atualizar todo o registro do médico.
    public MedicoResponseDTO atualizar(Integer id, MedicoRequestDTO dto) {

        Medico medico = buscarMedico(id);

        medico.setNomeMedico(dto.nomeMedico());
        medico.setCrm(dto.crm());
        medico.setEspecialidade(dto.especialidade());
        medico.setTelefoneMedico(dto.telefoneMedico());
        medico.setEmailMedico(dto.emailMedico());

        Medico medicoAtualizado = medicoRepository.save(medico);

        return new MedicoResponseDTO(medicoAtualizado);
    }

    // Atualizar parcialmente o registro do médico.
    public MedicoResponseDTO atualizarParcial(Integer id, MedicoRequestDTO dto) {

        Medico medico = buscarMedico(id);

        if (dto.nomeMedico() != null) {
            medico.setNomeMedico(dto.nomeMedico());
        }

        if (dto.crm() != null) {
            medico.setCrm(dto.crm());
        }

        if (dto.especialidade() != null) {
            medico.setEspecialidade(dto.especialidade());
        }

        if (dto.telefoneMedico() != null) {
            medico.setTelefoneMedico(dto.telefoneMedico());
        }

        if (dto.emailMedico() != null) {
            medico.setEmailMedico(dto.emailMedico());
        }

        Medico medicoAtualizado = medicoRepository.save(medico);

        return new MedicoResponseDTO(medicoAtualizado);
    }

    // Deletar médico.
    public MedicoResponseDTO deletarMedico(Integer id) {

        Medico medico = buscarMedico(id);

        medicoRepository.delete(medico);

        return new MedicoResponseDTO(medico);
    }

    // Método auxiliar para buscar médico.
    public Medico buscarMedico(Integer id) {

        return medicoRepository.findById(id)
                .orElseThrow(() ->
                        new MedicoNotFoundException("Médico não encontrado"));
    }
}
