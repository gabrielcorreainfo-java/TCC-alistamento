package com.example.tcc_alistamento.dto;

import com.example.tcc_alistamento.model.PedidoSenha;

import java.time.LocalDateTime;

// Pedido de nova senha, mostrado ao administrador
public record PedidoSenhaResponseDTO(Integer id, String email, LocalDateTime dataPedido, String status) {

    public PedidoSenhaResponseDTO(PedidoSenha pedido) {
        this(pedido.getId(), pedido.getEmail(), pedido.getDataPedido(), pedido.getStatus());
    }
}