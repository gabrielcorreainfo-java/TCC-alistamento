package com.example.tcc_alistamento.dto;

import com.example.tcc_alistamento.model.Medico;

public record MedicoResponseDTO(Integer id,
                                String nomeMedico,
                                String crm,
                                String especialidade,
                                String telefoneMedico,
                                String emailMedico) {
    public MedicoResponseDTO(Medico medico) {
        this(
                medico.getId(),
                medico.getNomeMedico(),
                medico.getCrm(),
                medico.getEspecialidade(),
                medico.getTelefoneMedico(),
                medico.getEmailMedico()
        );
    }
}
