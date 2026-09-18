package com.example.tcc_alistamento.repository;

import com.example.tcc_alistamento.model.Administrador;
import com.example.tcc_alistamento.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

/*
 * Repository responsável pelo acesso aos dados da entidade Usuario.
 *
 * O JpaRepository já fornece métodos prontos para realizar operações
 * no banco de dados, como:
 *
 * save()      -> salvar um usuário
 * findAll()   -> buscar todos os usuários
 * findById()  -> buscar usuário pelo ID
 * delete()    -> remover usuário
 *
 * O primeiro parâmetro do JpaRepository representa a entidade que será gerenciada.
 * O segundo parâmetro representa o tipo da chave primária da entidade.
 *
 * Neste caso:
 * Usuario -> entidade que representa a tabela usuario
 * Long    -> tipo do campo id_usuario
 */

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByEmail(String email);

}
