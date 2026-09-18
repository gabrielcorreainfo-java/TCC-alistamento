package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.AlistamentoResponseDTO;
import com.example.tcc_alistamento.dto.UsuarioRequestDTO;
import com.example.tcc_alistamento.dto.UsuarioResponseDTO;
import com.example.tcc_alistamento.exceptions.AlistamentoNotFoundException;
import com.example.tcc_alistamento.model.Usuario;
import com.example.tcc_alistamento.exceptions.UsuarioNotFoundException;
import com.example.tcc_alistamento.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/*
 * Camada responsável pelas regras de negócio do Usuario.
 * Faz a comunicação entre Controller e Repository.
 */


@Service
public class UsuarioService {

    // Repository responsável pelo acesso aos dados do Usuario no banco.
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Salva um usuário no banco de dados.
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // Retorna todos os usuários cadastrados.
    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(dto,usuario,"id");
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(usuario);

    }

    public List<UsuarioResponseDTO> listarTodos(){
        return usuarioRepository.findAll().
                stream()
                .map(UsuarioResponseDTO::new)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Integer id) {
        Usuario usuarioExistente = buscarUsuarioPorId(id);
        return new UsuarioResponseDTO(usuarioExistente);
    }



    public Usuario buscarUsuarioPorId(Integer id){
        //  se nao encontrar usuario ele lançara essa exceção
        return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrad"));
    }

    public UsuarioResponseDTO atualizarUsuario(Integer id, UsuarioRequestDTO dto){

        // Chama o método buscarPorId(), que procura o alistamento no banco
        // e retorna o objeto encontrado.
        // O resultado é armazenado na variável alistamentoExistente.
        Usuario usuarioExistente = buscarUsuarioPorId(id);

        //Ele copia todos os atributos de Usuario para usuarioExistente
        //Ele faz um set automático de todos os atributos menos o Id
        //BeanUtils.copyProperties(origem(JSON da requisição), destino(Objeto buscado no Banco));
        BeanUtils.copyProperties(dto, usuarioExistente, "id");
        usuarioRepository.save(usuarioExistente);

        return new UsuarioResponseDTO(usuarioRepository.save(usuarioExistente));
    }

    public UsuarioResponseDTO atualizarParcial(Integer id, UsuarioRequestDTO dto) {

        Usuario usuarioExistente = buscarUsuarioPorId(id);

        if (dto.nome() != null) {
            usuarioExistente.setNome(dto.nome());
        }

        if (dto.dataNascimento() != null) {
            usuarioExistente.setDataNascimento(dto.dataNascimento());
        }

        if (dto.email() != null) {
            usuarioExistente.setEmail(dto.email());
        }

        if (dto.telefone() != null) {
            usuarioExistente.setTelefone(dto.telefone());
        }

        if (dto.cpf() != null) {
            usuarioExistente.setCpf(dto.cpf());
        }

       usuarioRepository.save(usuarioExistente);
        return new UsuarioResponseDTO(usuarioExistente);

    }

        public UsuarioResponseDTO deletarUsuario(Integer id) {
            Usuario usuarioExistente =  buscarUsuarioPorId(id);

            usuarioRepository.delete(usuarioExistente);

            return new UsuarioResponseDTO(usuarioExistente);
        }

    public AlistamentoResponseDTO buscarAlistamentoDoUsuario(Integer idUsuario){
        Usuario usuario = buscarUsuarioPorId(idUsuario);

        if (usuario.getAlistamento() == null) {
            throw new AlistamentoNotFoundException("Usuário ainda não possui alistamento");
        }

        return new AlistamentoResponseDTO(usuario.getAlistamento());
    }

    }

