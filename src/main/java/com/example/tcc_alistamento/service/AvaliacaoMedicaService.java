package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.AvaliacaoMedicaRequestDTO;
import com.example.tcc_alistamento.dto.AvaliacaoMedicaResponseDTO;
import com.example.tcc_alistamento.exceptions.AlistamentoNotFoundException;
import com.example.tcc_alistamento.exceptions.AvaliacaoMedicaNotFoundException;
import com.example.tcc_alistamento.exceptions.LocalNotFoundException;
import com.example.tcc_alistamento.exceptions.MedicoNotFoundException;
import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import com.example.tcc_alistamento.infra.security.ControleAcesso;
import com.example.tcc_alistamento.model.Alistamento;
import com.example.tcc_alistamento.model.AvaliacaoMedica;
import com.example.tcc_alistamento.model.Local;
import com.example.tcc_alistamento.model.Medico;
import com.example.tcc_alistamento.repository.AlistamentoRepository;
import com.example.tcc_alistamento.repository.AvaliacaoMedicaRepository;
import com.example.tcc_alistamento.repository.LocalRepository;
import com.example.tcc_alistamento.repository.MedicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoMedicaService {

    private final AvaliacaoMedicaRepository avaliacaoMedicaRepository;
    private final AlistamentoRepository alistamentoRepository;
    private final MedicoRepository medicoRepository;
    private final LocalRepository localRepository;
    private final ControleAcesso controleAcesso;

    public AvaliacaoMedicaService(
            AvaliacaoMedicaRepository avaliacaoMedicaRepository,
            AlistamentoRepository alistamentoRepository,
            MedicoRepository medicoRepository,
            LocalRepository localRepository,
            ControleAcesso controleAcesso) {

        this.avaliacaoMedicaRepository = avaliacaoMedicaRepository;
        this.alistamentoRepository = alistamentoRepository;
        this.medicoRepository = medicoRepository;
        this.localRepository = localRepository;
        this.controleAcesso = controleAcesso;
    }

    // Registro da avaliação: só MEDICO
    public AvaliacaoMedicaResponseDTO salvar(AvaliacaoMedicaRequestDTO dto) {

        Alistamento alistamento = buscarAlistamento(dto.idAlistamento());

        // A avaliação só pode ser registrada para quem tem avaliação agendada
        if (alistamento.getAgendamento() == null) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "Este cidadão não possui avaliação agendada.");
        }

        // O médico logado registra a avaliação em nome dele mesmo (não de outro médico)
        Integer idMedico = controleAcesso.ehMedico() ? controleAcesso.atual().getId() : dto.idMedico();
        Medico medico = buscarMedico(idMedico);
        Local local = buscarLocal(dto.idLocal());

        AvaliacaoMedica avaliacao = new AvaliacaoMedica();
        avaliacao.setDataAvaliacao(dto.dataAvaliacao());
        avaliacao.setResultado(dto.resultado());
        avaliacao.setObservacoes(dto.observacoes());
        avaliacao.setAlistamento(alistamento);
        avaliacao.setMedico(medico);
        avaliacao.setLocal(local);

        return new AvaliacaoMedicaResponseDTO(avaliacaoMedicaRepository.save(avaliacao));
    }

    // Lista completa: só ADMIN e MEDICO
    public List<AvaliacaoMedicaResponseDTO> listar() {
        return avaliacaoMedicaRepository.findAll()
                .stream()
                .map(AvaliacaoMedicaResponseDTO::new)
                .toList();
    }

    // O cidadão só vê a PRÓPRIA avaliação
    public AvaliacaoMedicaResponseDTO buscarPorId(Integer id) {
        AvaliacaoMedica avaliacao = buscarAvaliacao(id);
        controleAcesso.exigirDonoDoAlistamento(avaliacao.getAlistamento());
        return new AvaliacaoMedicaResponseDTO(avaliacao);
    }

    // Edição completa: só ADMIN
    public AvaliacaoMedicaResponseDTO atualizar(Integer id, AvaliacaoMedicaRequestDTO dto) {

        AvaliacaoMedica avaliacao = buscarAvaliacao(id);
        Alistamento alistamento = buscarAlistamento(dto.idAlistamento());
        Medico medico = buscarMedico(dto.idMedico());
        Local local = buscarLocal(dto.idLocal());

        avaliacao.setDataAvaliacao(dto.dataAvaliacao());
        avaliacao.setResultado(dto.resultado());
        avaliacao.setObservacoes(dto.observacoes());
        avaliacao.setAlistamento(alistamento);
        avaliacao.setMedico(medico);
        avaliacao.setLocal(local);

        return new AvaliacaoMedicaResponseDTO(avaliacaoMedicaRepository.save(avaliacao));
    }

    // Edição parcial: só ADMIN
    public AvaliacaoMedicaResponseDTO atualizarParcial(Integer id, AvaliacaoMedicaRequestDTO dto) {

        AvaliacaoMedica avaliacao = buscarAvaliacao(id);

        if (dto.dataAvaliacao() != null) {
            avaliacao.setDataAvaliacao(dto.dataAvaliacao());
        }
        if (dto.resultado() != null) {
            avaliacao.setResultado(dto.resultado());
        }
        if (dto.observacoes() != null) {
            avaliacao.setObservacoes(dto.observacoes());
        }
        if (dto.idAlistamento() != null) {
            avaliacao.setAlistamento(buscarAlistamento(dto.idAlistamento()));
        }
        if (dto.idMedico() != null) {
            avaliacao.setMedico(buscarMedico(dto.idMedico()));
        }
        if (dto.idLocal() != null) {
            avaliacao.setLocal(buscarLocal(dto.idLocal()));
        }

        return new AvaliacaoMedicaResponseDTO(avaliacaoMedicaRepository.save(avaliacao));
    }

    // Exclusão: só ADMIN
    public AvaliacaoMedicaResponseDTO deletar(Integer id) {
        AvaliacaoMedica avaliacao = buscarAvaliacao(id);
        avaliacaoMedicaRepository.delete(avaliacao);
        return new AvaliacaoMedicaResponseDTO(avaliacao);
    }

    // ----- Métodos auxiliares -----

    private AvaliacaoMedica buscarAvaliacao(Integer id) {
        return avaliacaoMedicaRepository.findById(id)
                .orElseThrow(() -> new AvaliacaoMedicaNotFoundException("Avaliação médica não encontrada"));
    }

    private Alistamento buscarAlistamento(Integer id) {
        return alistamentoRepository.findById(id)
                .orElseThrow(() -> new AlistamentoNotFoundException("Alistamento não encontrado"));
    }

    private Local buscarLocal(Integer id) {
        return localRepository.findById(id)
                .orElseThrow(() -> new LocalNotFoundException("Local não encontrado"));
    }

    private Medico buscarMedico(Integer id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new MedicoNotFoundException("Médico não encontrado"));
    }
}