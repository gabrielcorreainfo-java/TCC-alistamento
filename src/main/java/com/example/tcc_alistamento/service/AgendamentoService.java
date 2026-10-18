package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.AgendamentoRequestDTO;
import com.example.tcc_alistamento.dto.AgendamentoResponseDTO;
import com.example.tcc_alistamento.exceptions.AgendamentoNotFoundException;
import com.example.tcc_alistamento.exceptions.AlistamentoNotFoundException;
import com.example.tcc_alistamento.exceptions.LocalNotFoundException;
import com.example.tcc_alistamento.exceptions.MedicoNotFoundException;
import com.example.tcc_alistamento.infra.security.ControleAcesso;
import com.example.tcc_alistamento.model.Agendamento;
import com.example.tcc_alistamento.model.Alistamento;
import com.example.tcc_alistamento.model.Local;
import com.example.tcc_alistamento.model.Medico;
import com.example.tcc_alistamento.repository.AgendamentoRepository;
import com.example.tcc_alistamento.repository.AlistamentoRepository;
import com.example.tcc_alistamento.repository.LocalRepository;
import com.example.tcc_alistamento.repository.MedicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final AlistamentoRepository alistamentoRepository;
    private final LocalRepository localRepository;
    private final MedicoRepository medicoRepository;
    private final ControleAcesso controleAcesso;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            AlistamentoRepository alistamentoRepository,
            LocalRepository localRepository,
            MedicoRepository medicoRepository,
            ControleAcesso controleAcesso) {

        this.agendamentoRepository = agendamentoRepository;
        this.alistamentoRepository = alistamentoRepository;
        this.localRepository = localRepository;
        this.medicoRepository = medicoRepository;
        this.controleAcesso = controleAcesso;
    }

    // Criação do agendamento: só ADMIN
    public AgendamentoResponseDTO salvar(AgendamentoRequestDTO dto) {

        Alistamento alistamento = buscarAlistamentoPorId(dto.idAlistamento());
        Local local = buscarLocalPorId(dto.idLocal());
        Medico medico = buscarMedicoPorId(dto.idMedico());

        Agendamento agendamento = new Agendamento();
        agendamento.setDataAgendamento(dto.dataAgendamento());
        agendamento.setHorario(dto.horario());
        agendamento.setAlistamento(alistamento);
        agendamento.setLocal(local);
        agendamento.setMedico(medico);

        return new AgendamentoResponseDTO(agendamentoRepository.save(agendamento));
    }

    // Lista completa: só ADMIN e MEDICO
    public List<AgendamentoResponseDTO> listar() {
        return agendamentoRepository.findAll()
                .stream()
                .map(AgendamentoResponseDTO::new)
                .toList();
    }

    // O cidadão só vê o PRÓPRIO agendamento
    public AgendamentoResponseDTO buscarPorId(Integer id) {
        Agendamento agendamento = buscarAgendamentoPorId(id);
        controleAcesso.exigirDonoDoAlistamento(agendamento.getAlistamento());
        return new AgendamentoResponseDTO(agendamento);
    }

    // Edição completa: só ADMIN
    public AgendamentoResponseDTO atualizar(Integer id, AgendamentoRequestDTO dto) {

        Agendamento agendamento = buscarAgendamentoPorId(id);
        Alistamento alistamento = buscarAlistamentoPorId(dto.idAlistamento());
        Local local = buscarLocalPorId(dto.idLocal());

        agendamento.setDataAgendamento(dto.dataAgendamento());
        agendamento.setHorario(dto.horario());
        agendamento.setAlistamento(alistamento);
        agendamento.setLocal(local);

        return new AgendamentoResponseDTO(agendamentoRepository.save(agendamento));
    }

    // Edição parcial: só ADMIN
    public AgendamentoResponseDTO atualizarParcial(Integer id, AgendamentoRequestDTO dto) {

        Agendamento agendamento = buscarAgendamentoPorId(id);

        if (dto.dataAgendamento() != null) {
            agendamento.setDataAgendamento(dto.dataAgendamento());
        }
        if (dto.horario() != null) {
            agendamento.setHorario(dto.horario());
        }
        if (dto.idAlistamento() != null) {
            agendamento.setAlistamento(buscarAlistamentoPorId(dto.idAlistamento()));
        }
        if (dto.idLocal() != null) {
            agendamento.setLocal(buscarLocalPorId(dto.idLocal()));
        }

        return new AgendamentoResponseDTO(agendamentoRepository.save(agendamento));
    }

    // O cidadão confirma presença apenas no PRÓPRIO agendamento
    public AgendamentoResponseDTO confirmarPresenca(Integer id) {
        Agendamento agendamento = buscarAgendamentoPorId(id);
        controleAcesso.exigirDonoDoAlistamento(agendamento.getAlistamento());

        agendamento.setConfirmado(true);
        return new AgendamentoResponseDTO(agendamentoRepository.save(agendamento));
    }

    // Exclusão: só ADMIN
    public AgendamentoResponseDTO deletar(Integer id) {
        Agendamento agendamento = buscarAgendamentoPorId(id);
        agendamentoRepository.delete(agendamento);
        return new AgendamentoResponseDTO(agendamento);
    }

    // ----- Métodos auxiliares -----

    private Agendamento buscarAgendamentoPorId(Integer id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new AgendamentoNotFoundException("Agendamento não encontrado"));
    }

    private Alistamento buscarAlistamentoPorId(Integer id) {
        return alistamentoRepository.findById(id)
                .orElseThrow(() -> new AlistamentoNotFoundException("Alistamento não encontrado"));
    }

    private Local buscarLocalPorId(Integer id) {
        return localRepository.findById(id)
                .orElseThrow(() -> new LocalNotFoundException("Local não encontrado"));
    }

    private Medico buscarMedicoPorId(Integer id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new MedicoNotFoundException("Médico não encontrado"));
    }
}