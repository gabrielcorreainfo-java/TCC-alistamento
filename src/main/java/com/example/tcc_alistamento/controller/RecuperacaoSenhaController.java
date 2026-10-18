package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.PedidoSenhaRequestDTO;
import com.example.tcc_alistamento.dto.PedidoSenhaResponseDTO;
import com.example.tcc_alistamento.service.RecuperacaoSenhaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recuperacao-senha")
public class RecuperacaoSenhaController {

    private final RecuperacaoSenhaService recuperacaoSenhaService;

    public RecuperacaoSenhaController(RecuperacaoSenhaService recuperacaoSenhaService) {
        this.recuperacaoSenhaService = recuperacaoSenhaService;
    }

    // Público: tela "Esqueci minha senha". Resposta sempre igual (não revela se o e-mail existe).
    @PostMapping
    public ResponseEntity<Void> solicitar(@RequestBody PedidoSenhaRequestDTO dto) {
        recuperacaoSenhaService.solicitar(dto.email());
        return ResponseEntity.noContent().build();
    }

    // Administrador: pedidos pendentes
    @GetMapping
    public List<PedidoSenhaResponseDTO> listar() {
        return recuperacaoSenhaService.listarPendentes();
    }

    // Administrador: marca o pedido como atendido
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Void> concluir(@PathVariable Integer id) {
        recuperacaoSenhaService.concluir(id);
        return ResponseEntity.noContent().build();
    }
}