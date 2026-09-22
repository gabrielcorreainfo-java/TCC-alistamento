package com.example.tcc_alistamento.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Avaliacaomedica")
public class AvaliacaoMedica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avaliacao")
    private Integer id;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDate dataAvaliacao;

    @Column(name = "resultado", nullable = false, length = 100)
    private String resultado;

    @Column(name = "observacoes")
    private String observacoes;

    @OneToOne
    @JoinColumn(name = "id_alistamento", nullable = false)
    private Alistamento alistamento;

    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "id_local", nullable = false)
    private Local local;
}
