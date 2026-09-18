package com.example.tcc_alistamento.repository;

import com.example.tcc_alistamento.model.AvaliacaoMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoMedicaRepository extends JpaRepository<AvaliacaoMedica,Integer> {
}
