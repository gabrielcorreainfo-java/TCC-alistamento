package com.example.tcc_alistamento.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "nome_pai", length = 100)
    private String nomePai;

    @Column(name = "nome_mae", length = 100)
    private String nomeMae;

    @Column(name = "estado_civil", length = 20)
    private String estadoCivil;

    @Column(length = 2)
    private String uf;

    @Column(length = 50)
    private String escolaridade;

    @Column(length = 20)
    private String rg;

    @Column(name = "local_nascimento", length = 100)
    private String localNascimento;

    @Column(length = 10)
    private String cep;

    @Column(length = 100)
    private String bairro;

    @Column(length = 100)
    private String municipio;

    @Column(name = "pais_residencia", length = 50)
    private String paisResidencia;

    @Column(name = "zona_residencial", length = 50)
    private String zonaResidencial;

    @Column(name = "numero_residencia", length = 10)
    private String numeroResidencia;

    @Column(length = 150)
    private String logradouro;

    @Column(length = 50)
    private String estado;

    public Usuario(String login, String password){
        this.email=login;
        this.senha=password;
    }


    //Cada usuário possui um único alistamento.
    // O relacionamento é controlado pelo atributo "usuario" da entidade Alistamento.
    @JsonIgnore
    @OneToOne(mappedBy = "usuario")
    private Alistamento alistamento;

    }
