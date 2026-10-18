package com.example.tcc_alistamento.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/*
Pedido de nova senha feito na tela "Esqueci minha senha".
O administrador vê os pedidos pendentes, redefine a senha do cidadão
e marca o pedido como atendido.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
// "Pedidosenha" (sem a segunda maiúscula), pelo mesmo motivo de "Avaliacaomedica":
// o Spring transformaria "PedidoSenha" em "pedido_senha"
@Table(name = "Pedidosenha")
public class PedidoSenha {

    public static final String PENDENTE = "Pendente";
    public static final String ATENDIDO = "Atendido";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(name = "data_pedido", nullable = false)
    private LocalDateTime dataPedido;

    @Column(nullable = false, length = 20)
    private String status = PENDENTE;

    @Column(name = "data_atendimento")
    private LocalDateTime dataAtendimento;
}