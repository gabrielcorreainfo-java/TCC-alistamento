package com.example.tcc_alistamento.dto;

import com.example.tcc_alistamento.model.Alistamento;

import java.util.List;

public record AlistamentoCompletoResponseDTO(AlistamentoResponseDTO alistamento,
                                             List<DocumentoResponseDTO> documentos,
                                             AgendamentoResponseDTO agendamento,
                                             AvaliacaoMedicaResponseDTO avaliacaoMedica) {
    public AlistamentoCompletoResponseDTO(Alistamento alistamento, List<DocumentoResponseDTO> documentos) {
        this(
                new AlistamentoResponseDTO(alistamento),
                documentos,
                alistamento.getAgendamento() != null
                        ? new AgendamentoResponseDTO(alistamento.getAgendamento())
                        : null,
                alistamento.getAvaliacaoMedica() != null
                        ? new AvaliacaoMedicaResponseDTO(alistamento.getAvaliacaoMedica())
                        : null

               /* Se existir agendamento:
                cria AgendamentoResponseDTO
                Se não existir:
                retorna null

                */
        );
    }
}
