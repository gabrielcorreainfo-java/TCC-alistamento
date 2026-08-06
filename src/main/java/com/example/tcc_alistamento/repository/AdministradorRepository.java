package com.example.tcc_alistamento.repository;

import com.example.tcc_alistamento.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministradorRepository extends JpaRepository<Administrador, Integer>{
    Administrador findByEmailAdmin(String emailAdmin);
}
