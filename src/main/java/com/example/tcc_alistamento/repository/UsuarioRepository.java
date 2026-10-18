package com.example.tcc_alistamento.repository;

import com.example.tcc_alistamento.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Repository responsável pelo acesso aos dados da entidade Usuario.
 * O JpaRepository já fornece save(), findAll(), findById() e delete().
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByEmail(String email);

    // O CPF fica criptografado no banco, então a busca é feita pela "impressão digital" (hash)
    boolean existsByCpfHash(String cpfHash);

    // Mesma verificação, ignorando o próprio usuário (usada ao editar)
    boolean existsByCpfHashAndIdNot(String cpfHash, Integer id);
}