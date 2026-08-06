package com.example.tcc_alistamento.repository;

import com.example.tcc_alistamento.entity.Alistamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlistamentoRepository extends JpaRepository<Alistamento, Integer> {
}
