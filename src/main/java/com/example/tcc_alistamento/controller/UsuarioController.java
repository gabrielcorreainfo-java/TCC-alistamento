package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.AlistamentoResponseDTO;
import com.example.tcc_alistamento.dto.UsuarioRequestDTO;
import com.example.tcc_alistamento.dto.UsuarioResponseDTO;
import com.example.tcc_alistamento.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 Controller responsável pelas requisições relacionadas aos cidadãos.
 O login fica no AuthenticationController (/auth/login), que é único
 para os três perfis.
 */
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Cadastro público. O @Valid aplica as validações do UsuarioRequestDTO.
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvar(dto));
    }

    // Lista de todos os cidadãos: só ADMIN e MEDICO
    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listarTodos();
    }

    // O cidadão só consegue buscar o próprio cadastro (ControleAcesso)
    @GetMapping("/{id}")
    public UsuarioResponseDTO buscarPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id);
    }

    @GetMapping("/{id}/alistamento")
    public AlistamentoResponseDTO buscarAlistamento(@PathVariable Integer id) {
        return usuarioService.buscarAlistamentoDoUsuario(id);
    }

    // Edição pelo administrador (inclusive para redefinir a senha)
    @PutMapping("/{id}")
    public UsuarioResponseDTO atualizarUsuario(@PathVariable Integer id, @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.atualizarUsuario(id, dto);
    }

    @PatchMapping("/{id}")
    public UsuarioResponseDTO atualizarParcial(@PathVariable Integer id, @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.atualizarParcial(id, dto);
    }

    @DeleteMapping("/{id}")
    public UsuarioResponseDTO deletarUsuario(@PathVariable Integer id) {
        return usuarioService.deletarUsuario(id);
    }
}