package com.example.tcc_alistamento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TipoDocumento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_documento")
    private Integer idTipoDocumento;

    @Column(name = "nome_tipo", nullable = false, length = 100)
    private String nomeTipo;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
}
