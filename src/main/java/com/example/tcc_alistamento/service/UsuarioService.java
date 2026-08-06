package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.entity.Usuario;
import com.example.tcc_alistamento.exceptions.UsuarioNotFoundException;
import com.example.tcc_alistamento.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.List;

/*
 * Camada responsável pelas regras de negócio do Usuario.
 * Faz a comunicação entre Controller e Repository.
 */


@Service
public class UsuarioService {

    // Repository responsável pelo acesso aos dados do Usuario no banco.
    private final UsuarioRepository usuarioRepository;

    // Salva um usuário no banco de dados.
    public UsuarioService(UsuarioRepository usuarioRepository){this.usuarioRepository = usuarioRepository;
    }



    // Retorna todos os usuários cadastrados.
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);

    }

    public List<Usuario> listarTodos(){
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Integer id) {
     //  se nao encontrar usuario ele lançara essa exceção
        return usuarioRepository.findById(id).orElseThrow(() ->
                new UsuarioNotFoundException("Usuário não encontrado")
        );
    }

    public Usuario atualizarUsuario(Integer id, Usuario usuario){

        // Chama o método buscarPorId(), que procura o alistamento no banco
        // e retorna o objeto encontrado.
        // O resultado é armazenado na variável alistamentoExistente.
        Usuario usuarioExistente = buscarPorId(id);

        if(usuarioExistente == null){
            return null;
        }

        //Ele copia todos os atributos de Usuario para usuarioExistente
        //Ele faz um set automático de todos os atributos menos o Id
        //BeanUtils.copyProperties(origem(JSON da requisição), destino(Objeto buscado no Banco));
        BeanUtils.copyProperties(usuario, usuarioExistente, "id");

        return usuarioRepository.save(usuarioExistente);
    }

    public Usuario atualizarParcial(Integer id, Usuario usuario) {


        Usuario usuarioExistente = buscarPorId(id);

        if (usuarioExistente == null) {
            return null;
        }

        if (usuario.getNome() != null) {
            usuarioExistente.setNome(usuario.getNome());
        }

        if (usuario.getDataNascimento() != null) {
            usuarioExistente.setDataNascimento(usuario.getDataNascimento());
        }

        if (usuario.getEmail() != null) {
            usuarioExistente.setEmail(usuario.getEmail());
        }

        if (usuario.getSenha() != null) {
            usuarioExistente.setSenha(usuario.getSenha());
        }

        if (usuario.getTelefone() != null) {
            usuarioExistente.setTelefone(usuario.getTelefone());
        }

        if (usuario.getCpf() != null) {
            usuarioExistente.setCpf(usuario.getCpf());
        }

        if (usuario.getNomePai() != null) {
            usuarioExistente.setNomePai(usuario.getNomePai());
        }

        if (usuario.getNomeMae() != null) {
            usuarioExistente.setNomeMae(usuario.getNomeMae());
        }

        if (usuario.getEstadoCivil() != null) {
            usuarioExistente.setEstadoCivil(usuario.getEstadoCivil());
        }

        if (usuario.getUf() != null) {
            usuarioExistente.setUf(usuario.getUf());
        }

        if (usuario.getEscolaridade() != null) {
            usuarioExistente.setEscolaridade(usuario.getEscolaridade());
        }

        if (usuario.getRg() != null) {
            usuarioExistente.setRg(usuario.getRg());
        }

        if (usuario.getLocalNascimento() != null) {
            usuarioExistente.setLocalNascimento(usuario.getLocalNascimento());
        }

        if (usuario.getCep() != null) {
            usuarioExistente.setCep(usuario.getCep());
        }

        if (usuario.getBairro() != null) {
            usuarioExistente.setBairro(usuario.getBairro());
        }

        if (usuario.getMunicipio() != null) {
            usuarioExistente.setMunicipio(usuario.getMunicipio());
        }

        if (usuario.getPaisResidencia() != null) {
            usuarioExistente.setPaisResidencia(usuario.getPaisResidencia());
        }

        if (usuario.getZonaResidencial() != null) {
            usuarioExistente.setZonaResidencial(usuario.getZonaResidencial());
        }

        if (usuario.getNumeroResidencia() != null) {
            usuarioExistente.setNumeroResidencia(usuario.getNumeroResidencia());
        }

        if (usuario.getLogradouro() != null) {
            usuarioExistente.setLogradouro(usuario.getLogradouro());
        }

        if (usuario.getEstado() != null) {
            usuarioExistente.setEstado(usuario.getEstado());
        }

        return usuarioRepository.save(usuarioExistente);

    }

        public Usuario deletarUsuario(Integer id) {

            Usuario usuarioExistente =  buscarPorId(id);

            if (usuarioExistente == null) {
                return null;
            }

            usuarioRepository.deleteById(id);

            return usuarioExistente;
        }

    }

