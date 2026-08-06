package com.example.tcc_alistamento.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Alistamento")
public class Alistamento {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_alistamento")
    private Integer id;

    @Column(name = "data_alistamento")
    private LocalDate dataAlistamento;

    @Column(length = 50)
    private String status;

    // A coluna id_usuario da tabela Alistamento guarda o ID do usuário relacionado.

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Muitos alistamentos podem estar relacionados a um administrador.
    @ManyToOne
    @JoinColumn(name = "id_admin")
    private Administrador administrador;

}
