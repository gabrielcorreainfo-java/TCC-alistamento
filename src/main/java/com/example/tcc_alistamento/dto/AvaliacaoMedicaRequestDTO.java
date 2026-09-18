package com.example.tcc_alistamento.dto;

import java.time.LocalDate;

public record AvaliacaoMedicaRequestDTO(LocalDate dataAvaliacao,
                                        String resultado,
                                        String observacoes,
                                        Integer idAlistamento,
                                        Integer idMedico,
                                        Integer idLocal) {
}
