package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.AvaliacaoMedicaRequestDTO;
import com.example.tcc_alistamento.dto.AvaliacaoMedicaResponseDTO;
import com.example.tcc_alistamento.exceptions.AlistamentoNotFoundException;
import com.example.tcc_alistamento.exceptions.AvaliacaoMedicaNotFoundException;
import com.example.tcc_alistamento.exceptions.LocalNotFoundException;
import com.example.tcc_alistamento.exceptions.MedicoNotFoundException;
import com.example.tcc_alistamento.model.Alistamento;
import com.example.tcc_alistamento.model.AvaliacaoMedica;
import com.example.tcc_alistamento.model.Local;
import com.example.tcc_alistamento.model.Medico;
import com.example.tcc_alistamento.repository.AlistamentoRepository;
import com.example.tcc_alistamento.repository.AvaliacaoMedicaRepository;
import com.example.tcc_alistamento.repository.LocalRepository;
import com.example.tcc_alistamento.repository.MedicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoMedicaService {
    private final AvaliacaoMedicaRepository avaliacaoMedicaRepository;
    private final AlistamentoRepository alistamentoRepository;
    private final MedicoRepository medicoRepository;
    private final LocalRepository localRepository;

    public AvaliacaoMedicaService(
            AvaliacaoMedicaRepository avaliacaoMedicaRepository,
            AlistamentoRepository alistamentoRepository,
            MedicoRepository medicoRepository,
            LocalRepository localRepository) {

        this.avaliacaoMedicaRepository = avaliacaoMedicaRepository;
        this.alistamentoRepository = alistamentoRepository;
        this.medicoRepository = medicoRepository;
        this.localRepository = localRepository;
    }

    public AvaliacaoMedicaResponseDTO salvar(AvaliacaoMedicaRequestDTO dto) {

        Alistamento alistamento = buscarAlistamento(dto.idAlistamento());
        Medico medico = buscarMedico(dto.idMedico());
        Local local = buscarLocal(dto.idLocal());

        AvaliacaoMedica avaliacao = new AvaliacaoMedica();

        avaliacao.setDataAvaliacao(dto.dataAvaliacao());
        avaliacao.setResultado(dto.resultado());
        avaliacao.setObservacoes(dto.observacoes());

        avaliacao.setAlistamento(alistamento);
        avaliacao.setMedico(medico);
        avaliacao.setLocal(local);

        AvaliacaoMedica avaliacaoSalva = avaliacaoMedicaRepository.save(avaliacao);

        return new AvaliacaoMedicaResponseDTO(avaliacaoSalva);
    }

    public List<AvaliacaoMedicaResponseDTO> listar() {

        return avaliacaoMedicaRepository.findAll()
                .stream()
                .map(AvaliacaoMedicaResponseDTO::new)
                .toList();
    }

    public AvaliacaoMedicaResponseDTO buscarPorId(Integer id) {

        AvaliacaoMedica avaliacao = buscarAvaliacao(id);

        return new AvaliacaoMedicaResponseDTO(avaliacao);
    }

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

        AvaliacaoMedica avaliacaoAtualizada = avaliacaoMedicaRepository.save(avaliacao);

        return new AvaliacaoMedicaResponseDTO(avaliacaoAtualizada);
    }

    public AvaliacaoMedicaResponseDTO atualizarParcial(
            Integer id,
            AvaliacaoMedicaRequestDTO dto) {

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
            Alistamento alistamento = buscarAlistamento(dto.idAlistamento());
            avaliacao.setAlistamento(alistamento);
        }

        if (dto.idMedico() != null) {
            Medico medico = buscarMedico(dto.idMedico());
            avaliacao.setMedico(medico);
        }

        if (dto.idLocal() != null) {
            Local local = buscarLocal(dto.idLocal());
            avaliacao.setLocal(local);
        }

        AvaliacaoMedica avaliacaoAtualizada = avaliacaoMedicaRepository.save(avaliacao);

        return new AvaliacaoMedicaResponseDTO(avaliacaoAtualizada);
    }

    public AvaliacaoMedicaResponseDTO deletar(Integer id) {

        AvaliacaoMedica avaliacao = buscarAvaliacao(id);

        avaliacaoMedicaRepository.delete(avaliacao);

        return new AvaliacaoMedicaResponseDTO(avaliacao);
    }

    // METODOA AUXILIADORES

    private AvaliacaoMedica buscarAvaliacao(Integer id) {

        return avaliacaoMedicaRepository
                .findById(id)
                .orElseThrow(() ->
                        new AvaliacaoMedicaNotFoundException(
                                "Avaliação médica não encontrada"));
    }

    private Alistamento buscarAlistamento(Integer id) {
        return alistamentoRepository.findById(id).orElseThrow(() ->
                        new AlistamentoNotFoundException("Alistamento não encontrado"));
    }
    private Local buscarLocal(Integer id){
        return localRepository.findById(id).orElseThrow(() ->
                new LocalNotFoundException("Local não encontrado"));
    }
    private Medico buscarMedico(Integer id){
        return medicoRepository.findById(id).orElseThrow(() ->
                new MedicoNotFoundException("Médico não encontrado"));
    }
}
