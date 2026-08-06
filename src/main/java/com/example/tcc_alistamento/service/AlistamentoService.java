package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.AlistamentoRequestDTO;
import com.example.tcc_alistamento.dto.AlistamentoResponseDTO;
import com.example.tcc_alistamento.entity.Administrador;
import com.example.tcc_alistamento.entity.Alistamento;
import com.example.tcc_alistamento.entity.Usuario;
import com.example.tcc_alistamento.exceptions.AdministradorNotFoundException;
import com.example.tcc_alistamento.exceptions.AlistamentoNotFoundException;
import com.example.tcc_alistamento.exceptions.UsuarioNotFoundException;
import com.example.tcc_alistamento.repository.AdministradorRepository;
import com.example.tcc_alistamento.repository.AlistamentoRepository;
import com.example.tcc_alistamento.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlistamentoService {
    private final AlistamentoRepository alistamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AdministradorRepository administradorRepository;


    public AlistamentoService(AlistamentoRepository alistamentoRepository, UsuarioRepository usuarioRepository, AdministradorRepository administradorRepository) {
        this.alistamentoRepository = alistamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.administradorRepository = administradorRepository;
    }

    public AlistamentoResponseDTO salvar(AlistamentoRequestDTO dto){

        Usuario usuario = buscarUsuario(dto.idUsuario());
        Administrador administrador = buscarAdministrador(dto.idAdministrador());

        Alistamento alistamento = new Alistamento();
        alistamento.setDataAlistamento(dto.dataAlistamento());
        alistamento.setStatus(dto.status());
        alistamento.setUsuario(usuario);
        alistamento.setAdministrador(administrador);

        Alistamento alistamentoSalvo = alistamentoRepository.save(alistamento);

        return new AlistamentoResponseDTO(alistamento);
    }

    public List<AlistamentoResponseDTO> listar(){
        return alistamentoRepository.findAll(). // Busca no Banco
                stream()// Cria um manipulador de coleções
                .map(AlistamentoResponseDTO::new) // Converte cada entidade para DTO usando o construtor da classe
                .toList(); // retorna tudo em uma lista
    }

    public AlistamentoResponseDTO buscarPorId(Integer id){
        Alistamento alistamento = alistamentoRepository.findById(id).orElseThrow(()-> new AlistamentoNotFoundException("Alistamento não encontrado"));
        return new AlistamentoResponseDTO(alistamento);
    }

    public AlistamentoResponseDTO atualizar(Integer id, AlistamentoRequestDTO dto){

        Alistamento alistamentoExistente = alistamentoRepository.findById(id).orElseThrow(()-> new AlistamentoNotFoundException("Alistamento não Encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.idUsuario()).orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));

        Administrador administrador = administradorRepository.findById(dto.idAdministrador()).orElseThrow(() -> new AdministradorNotFoundException("Administrador não encontrado"));

        alistamentoExistente.setDataAlistamento(dto.dataAlistamento());
        alistamentoExistente.setStatus(dto.status());
        alistamentoExistente.setUsuario(usuario);
        alistamentoExistente.setAdministrador(administrador);
        Alistamento alistamentoAtualizado = alistamentoRepository.save(alistamentoExistente);

        return new AlistamentoResponseDTO(alistamentoAtualizado);

    }

    public AlistamentoResponseDTO atualizarParcial(Integer id, AlistamentoRequestDTO dto){
        Alistamento alistamentoExistente = alistamentoRepository.findById(id).orElseThrow(() -> new AlistamentoNotFoundException("Alistamento não encontrado"));

        if (dto.dataAlistamento() != null) {alistamentoExistente.setDataAlistamento(dto.dataAlistamento());
        }

        if (dto.status() != null) {alistamentoExistente.setStatus(dto.status());
        }

        if (dto.idUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(dto.idUsuario()).orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));
            alistamentoExistente.setUsuario(usuario);
        }

        if (dto.idAdministrador() != null) {
            Administrador administrador = administradorRepository.findById(dto.idAdministrador()).orElseThrow(() -> new AdministradorNotFoundException("Administrador não encontrado"));
            alistamentoExistente.setAdministrador(administrador);
        }

        Alistamento alistamentoAtualizado = alistamentoRepository.save(alistamentoExistente);

        return new AlistamentoResponseDTO(alistamentoAtualizado);


    }

    public AlistamentoResponseDTO deletarAlistamento(Integer id){
        Alistamento alistamentoExistente = alistamentoRepository.findById(id).orElseThrow(() -> new AlistamentoNotFoundException("Alistamento não encontrado"));

        // Quebra o relacionamento entre Usuario e Alistamento
        Usuario usuario = alistamentoExistente.getUsuario();

        if (usuario != null) {
            usuario.setAlistamento(null);
            usuarioRepository.save(usuario);
        }

        alistamentoRepository.delete(alistamentoExistente);
        return new AlistamentoResponseDTO(alistamentoExistente);
    }

    private Usuario buscarUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new UsuarioNotFoundException("Usuário não encontrado"));
    }

    private Administrador buscarAdministrador(Integer id) {
        return administradorRepository.findById(id)
                .orElseThrow(() ->
                        new AdministradorNotFoundException("Administrador não encontrado"));
    }

    private Alistamento buscarAlistamento(Integer id) {
        return alistamentoRepository.findById(id)
                .orElseThrow(() ->
                        new AlistamentoNotFoundException("Alistamento não encontrado"));
    }
}
