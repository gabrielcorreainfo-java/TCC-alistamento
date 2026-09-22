package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.AgendamentoRequestDTO;
import com.example.tcc_alistamento.dto.AgendamentoResponseDTO;
import com.example.tcc_alistamento.exceptions.AgendamentoNotFoundException;
import com.example.tcc_alistamento.exceptions.AlistamentoNotFoundException;
import com.example.tcc_alistamento.exceptions.LocalNotFoundException;
import com.example.tcc_alistamento.exceptions.MedicoNotFoundException;
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


    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            AlistamentoRepository alistamentoRepository,
            LocalRepository localRepository,
            MedicoRepository medicoRepository) {

        this.agendamentoRepository = agendamentoRepository;
        this.alistamentoRepository = alistamentoRepository;
        this.localRepository = localRepository;
        this.medicoRepository = medicoRepository;

    }

    public AgendamentoResponseDTO salvar(AgendamentoRequestDTO dto) {

        Alistamento alistamento = buscarAlistamentoPorId(dto.idAlistamento());
        Local local = buscarLocalPorId(dto.idLocal());
        Medico medico = buscarMedicoPorId(dto.idMedico());
        Agendamento agendamento = new Agendamento();

        agendamento.setDataAgendamento(dto.dataAgendamento());
        agendamento.setHorario(dto.horario());

        agendamento.setAlistamento(alistamento);
        agendamento.setLocal(local);

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);

        return new AgendamentoResponseDTO(agendamentoSalvo);
    }

    public List<AgendamentoResponseDTO> listar() {

        return agendamentoRepository.findAll()
                .stream()
                .map(AgendamentoResponseDTO::new)
                .toList();
    }

    public AgendamentoResponseDTO buscarPorId(Integer id) {

        Agendamento agendamento = buscarAgendamentoPorId(id);

        return new AgendamentoResponseDTO(agendamento);
    }

    public AgendamentoResponseDTO atualizar(Integer id, AgendamentoRequestDTO dto) {

        Agendamento agendamento = buscarAgendamentoPorId(id);

        Alistamento alistamento = buscarAlistamentoPorId(dto.idAlistamento());

        Local local = buscarLocalPorId(dto.idLocal());

        agendamento.setDataAgendamento(dto.dataAgendamento());
        agendamento.setHorario(dto.horario());
        agendamento.setAlistamento(alistamento);
        agendamento.setLocal(local);

        Agendamento agendamentoAtualizado = agendamentoRepository.save(agendamento);

        return new AgendamentoResponseDTO(agendamentoAtualizado);
    }

    public AgendamentoResponseDTO atualizarParcial(Integer id, AgendamentoRequestDTO dto) {

        Agendamento agendamento =
                buscarAgendamentoPorId(id);

        if (dto.dataAgendamento() != null) {
            agendamento.setDataAgendamento(dto.dataAgendamento());
        }

        if (dto.horario() != null) {
            agendamento.setHorario(dto.horario());
        }

        if (dto.idAlistamento() != null) {

            Alistamento alistamento = buscarAlistamentoPorId(dto.idAlistamento());
            agendamento.setAlistamento(alistamento);
        }

        if (dto.idLocal() != null) {

            Local local = buscarLocalPorId(dto.idLocal());
            agendamento.setLocal(local);
        }

        Agendamento agendamentoAtualizado = agendamentoRepository.save(agendamento);

        return new AgendamentoResponseDTO(agendamentoAtualizado);
    }

    // usuário só confirma presença
    public AgendamentoResponseDTO confirmarPresenca(Integer id) {
        Agendamento agendamento = buscarAgendamentoPorId(id);
        agendamento.setConfirmado(true);

        Agendamento agendamentoAtualizado = agendamentoRepository.save(agendamento);
        return new AgendamentoResponseDTO(agendamentoAtualizado);
    }

    public AgendamentoResponseDTO deletar(Integer id) {

        Agendamento agendamento = buscarAgendamentoPorId(id);

        agendamentoRepository.delete(agendamento);

        return new AgendamentoResponseDTO(agendamento);
    }

    private Agendamento buscarAgendamentoPorId(Integer id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() ->
                        new AgendamentoNotFoundException("Agendamento não encontrado"));
    }

    private Alistamento buscarAlistamentoPorId(Integer id) {
        return alistamentoRepository.findById(id)
                .orElseThrow(() ->
                        new AlistamentoNotFoundException("Alistamento não encontrado"));
    }

    private Local buscarLocalPorId(Integer id) {
        return localRepository.findById(id)
                .orElseThrow(() ->
                        new LocalNotFoundException("Local não encontrado"));
    }
    private Medico buscarMedicoPorId(Integer id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new MedicoNotFoundException("Médico não encontrado"));
    }
}
