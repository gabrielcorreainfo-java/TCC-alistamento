package com.example.tcc_alistamento.dto;

public record MedicoRequestDTO(String nomeMedico,
                               String crm,
                               String especialidade,
                               String telefoneMedico,
                               String emailMedico,
                               String senhaMedico
) {
}
