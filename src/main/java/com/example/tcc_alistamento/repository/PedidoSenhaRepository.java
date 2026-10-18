package com.example.tcc_alistamento.repository;

import com.example.tcc_alistamento.model.PedidoSenha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoSenhaRepository extends JpaRepository<PedidoSenha, Integer> {

    // Pedidos ainda não atendidos, do mais antigo para o mais novo
    List<PedidoSenha> findByStatusOrderByDataPedidoAsc(String status);

    // Evita vários pedidos pendentes para o mesmo e-mail
    boolean existsByEmailIgnoreCaseAndStatus(String email, String status);
}