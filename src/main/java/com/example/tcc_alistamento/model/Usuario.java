package com.example.tcc_alistamento.model;

import com.example.tcc_alistamento.security.Criptografia;
import com.example.tcc_alistamento.security.DadoPessoalConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
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

    // LGPD: gravado criptografado (AES) no banco
    @Convert(converter = DadoPessoalConverter.class)
    @Column(nullable = false, length = 255)
    private String cpf;

    // "Impressão digital" do CPF (HMAC). Permite impedir CPF repetido
    // sem precisar descriptografar. Calculada automaticamente antes de salvar.
    @JsonIgnore
    @Column(name = "cpf_hash", nullable = false, unique = true, length = 64)
    private String cpfHash;

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

    // LGPD: gravado criptografado (AES) no banco
    @Convert(converter = DadoPessoalConverter.class)
    @Column(length = 255)
    private String rg;

    @Column(name = "local_nascimento", length = 100)
    private String localNascimento;

    // LGPD: gravado criptografado (AES) no banco
    @Convert(converter = DadoPessoalConverter.class)
    @Column(length = 255)
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

    // Masculino ou Feminino (define a modalidade do alistamento)
    @Column(nullable = false, length = 20)
    private String sexo;

    // Brasileira ou Brasileira naturalizada
    @Column(nullable = false, length = 30)
    private String nacionalidade;

    // LGPD: aceite da Política de Privacidade no cadastro
    @Column(name = "consentimento_lgpd", nullable = false)
    private Boolean consentimentoLgpd = false;

    // LGPD: quando o aceite foi dado (comprova o consentimento)
    @Column(name = "data_consentimento")
    private LocalDateTime dataConsentimento;

    public Usuario(String login, String password){
        this.email = login;
        this.senha = password;
    }

    // Cada usuário possui um único alistamento.
    // O relacionamento é controlado pelo atributo "usuario" da entidade Alistamento.
    @JsonIgnore
    @OneToOne(mappedBy = "usuario")
    private Alistamento alistamento;

    // Antes de inserir ou atualizar, recalcula a impressão digital do CPF
    @PrePersist
    @PreUpdate
    private void calcularHashCpf() {
        this.cpfHash = Criptografia.hash(this.cpf);
    }
}
