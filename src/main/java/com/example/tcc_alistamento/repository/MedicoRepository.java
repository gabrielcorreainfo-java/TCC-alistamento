package com.example.tcc_alistamento.repository;

import com.example.tcc_alistamento.model.Administrador;
import com.example.tcc_alistamento.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    Medico findByEmailMedico(String emailMedico);
}
