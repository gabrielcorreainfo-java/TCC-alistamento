package com.example.tcc_alistamento.repository;

import com.example.tcc_alistamento.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface AdministradorRepository extends JpaRepository<Administrador, Integer>{
    Administrador findByEmailAdmin(String emailAdmin);

}
