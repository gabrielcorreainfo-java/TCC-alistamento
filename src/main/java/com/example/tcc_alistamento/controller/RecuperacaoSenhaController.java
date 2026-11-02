package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.PedidoSenhaRequestDTO;
import com.example.tcc_alistamento.dto.RedefinirSenhaDTO;
import com.example.tcc_alistamento.service.RecuperacaoSenhaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recuperacao-senha")
public class RecuperacaoSenhaController {

    private final RecuperacaoSenhaService recuperacaoSenhaService;

    public RecuperacaoSenhaController(RecuperacaoSenhaService recuperacaoSenhaService) {
        this.recuperacaoSenhaService = recuperacaoSenhaService;
    }

    // Público: envia o código por e-mail. Resposta sempre igual (não revela se o e-mail existe).
    @PostMapping
    public ResponseEntity<Void> solicitar(@RequestBody PedidoSenhaRequestDTO dto) {
        recuperacaoSenhaService.solicitar(dto.email());
        return ResponseEntity.noContent().build();
    }

    // Público: confere o código e grava a senha nova
    @PostMapping("/redefinir")
    public ResponseEntity<Void> redefinir(@RequestBody RedefinirSenhaDTO dto) {
        recuperacaoSenhaService.redefinir(dto);
        return ResponseEntity.noContent().build();
    }
}