package com.example.tcc_alistamento.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Local")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_local")
    private Integer id;

    @Column(name = "nome_unidade", nullable = false, length = 100)
    private String nomeUnidade;

    @Column(name = "endereco_local", nullable = false, length = 150)
    private String enderecoLocal;

    @Column(name = "cidade_local", nullable = false, length = 100)
    private String cidadeLocal;

    @Column(name = "estado_local", nullable = false, length = 50)
    private String estadoLocal;

    @Column(name = "cep_local", nullable = false, length = 10)
    private String cepLocal;
}
