package com.example.tcc_alistamento.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoRequestDTO(LocalDate dataAgendamento,
                                    LocalTime horario,
                                    Integer idAlistamento,
                                    Integer idLocal) {
}
