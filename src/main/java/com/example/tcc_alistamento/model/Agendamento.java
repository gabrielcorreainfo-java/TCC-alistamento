package com.example.tcc_alistamento.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Agendamento")
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Integer id;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDate dataAgendamento;

    @Column(name = "horario", nullable = false)
    private LocalTime horario;

    @OneToOne
    @JoinColumn(name = "id_alistamento", nullable = false)
    private Alistamento alistamento;

    @ManyToOne
    @JoinColumn(name = "id_local", nullable = false)
    private Local local;

    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;

    @Column(nullable = false)
    private Boolean confirmado = false;

    @Column(nullable = false, length = 20)
    private String status = "Agendado";
}
