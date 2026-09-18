package com.example.tcc_alistamento.repository;

import com.example.tcc_alistamento.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
}
