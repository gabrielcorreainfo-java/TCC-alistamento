package com.example.tcc_alistamento.model;

import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Medico")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    private Integer id;

    @Column(name = "nome_medico", nullable = false, length = 100)
    private String nomeMedico;

    @Column(nullable = false, unique = true, length = 20)
    private String crm;

    @Column(length = 100)
    private String especialidade;

    @Column(name = "telefone_medico", length = 20)
    private String telefoneMedico;

    @Column(name = "senha_medico", nullable = false, length = 255)
    private String senhaMedico;

    @Column(name = "email_medico", length = 100)
    private String emailMedico;
}
