package com.example.tcc_alistamento.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "Administrador")
@AllArgsConstructor
@NoArgsConstructor

public class Administrador {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nomeAdmin;


    @Column(nullable = false, unique = true, length = 100)
    private String emailAdmin;


    @Column(nullable = false, length = 255)
    private String senhaAdmin;


// mappedBy = "administrador": informa que quem possui a chave estrangeira
// é o atributo "administrador" da entidade Alistamento.

    // Ignora esse atributo no JSON para não ter loop infinito
    @JsonIgnore
    @OneToMany(mappedBy = "administrador")
    private List<Alistamento> alistamentos;

}
