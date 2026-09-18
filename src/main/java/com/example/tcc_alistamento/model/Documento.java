package com.example.tcc_alistamento.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Documento")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Integer idDocumento;

    @Column(name = "numero_documento", nullable = false, length = 50)
    private String numeroDocumento;

    @Column(name = "numero_folha", length = 50)
    private String numeroFolha;

    @Column(name = "numero_livro", length = 50)
    private String numeroLivro;

    @Column(name = "data_emissao", nullable = false)
    private LocalDate dataEmissao;

    @Column(name = "orgao_emissor", nullable = false, length = 100)
    private String orgaoEmissor;

    @Column(name = "cidade_emissao", nullable = false, length = 100)
    private String cidadeEmissao;

    @Column(name = "estado_emissao", nullable = false, length = 50)
    private String estadoEmissao;

    @Column(name = "nome_arquivo", nullable = false, length = 100)
    private String nomeArquivo;

    @Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_alistamento", nullable = false)
    private Alistamento alistamento;

    @ManyToOne
    @JoinColumn(name = "id_tipo_documento", nullable = false)
    private TipoDocumento tipoDocumento;
}
