package com.example.tcc_alistamento.dto;

import com.example.tcc_alistamento.model.Agendamento;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoResponseDTO(Integer id,
                                     LocalDate dataAgendamento,
                                     LocalTime horario,
                                     AlistamentoResponseDTO alistamentoResponseDTO,
                                     LocalResponseDTO localResponseDTO,
                                     MedicoResponseDTO medicoResponseDTO,
                                     Boolean confirmado,
                                     String status) {
    public AgendamentoResponseDTO(Agendamento agendamento) {
        this(
                agendamento.getId(),
                agendamento.getDataAgendamento(),
                agendamento.getHorario(),
                new AlistamentoResponseDTO(agendamento.getAlistamento()),
                new LocalResponseDTO(agendamento.getLocal()),
                new MedicoResponseDTO(agendamento.getMedico()),
                agendamento.getConfirmado(),
                agendamento.getStatus()
        );
    }
}
