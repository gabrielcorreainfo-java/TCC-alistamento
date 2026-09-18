package com.example.tcc_alistamento.repository;

import com.example.tcc_alistamento.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento,Integer> {
    // Busque todos os Documento cujo alistamento.id seja igual ao valor informado.
    List<Documento> findByAlistamentoId(Integer idAlistamento);
}
