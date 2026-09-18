package com.example.tcc_alistamento.dto;

import com.example.tcc_alistamento.model.AvaliacaoMedica;

import java.time.LocalDate;

public record AvaliacaoMedicaResponseDTO(Integer id,
                                         LocalDate dataAvaliacao,
                                         String resultado,
                                         String observacoes,
                                         AlistamentoResponseDTO alistamentoResponseDTO,
                                         MedicoResponseDTO medicoResponseDTO,
                                         LocalResponseDTO localResponseDTO) {
    public AvaliacaoMedicaResponseDTO(AvaliacaoMedica avaliacaoMedica) {
        this(
                avaliacaoMedica.getId(),
                avaliacaoMedica.getDataAvaliacao(),
                avaliacaoMedica.getResultado(),
                avaliacaoMedica.getObservacoes(),
                new AlistamentoResponseDTO(avaliacaoMedica.getAlistamento()),
                new MedicoResponseDTO(avaliacaoMedica.getMedico()),
                new LocalResponseDTO(avaliacaoMedica.getLocal())
        );
    }
}
