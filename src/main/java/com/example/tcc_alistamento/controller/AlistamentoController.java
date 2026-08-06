package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.AlistamentoRequestDTO;
import com.example.tcc_alistamento.dto.AlistamentoResponseDTO;
import com.example.tcc_alistamento.entity.Alistamento;
import com.example.tcc_alistamento.entity.Usuario;
import com.example.tcc_alistamento.service.AlistamentoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alistamento")
public class AlistamentoController {
    private final AlistamentoService alistamentoService;

    public AlistamentoController(AlistamentoService alistamentoService) {
        this.alistamentoService = alistamentoService;
    }

    @PostMapping
    public AlistamentoResponseDTO cadastrar(@RequestBody AlistamentoRequestDTO dto) {
        return alistamentoService.salvar(dto);
    }

    @GetMapping
    public List<AlistamentoResponseDTO> listar() {
        return alistamentoService.listar();
    }

    @GetMapping("/{id}")
    public AlistamentoResponseDTO buscarPorId(
            @PathVariable Integer id) {

        return alistamentoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public AlistamentoResponseDTO atualizarAlistamento(
            @PathVariable Integer id,
            @RequestBody AlistamentoRequestDTO dto) {

        return alistamentoService.atualizar(id, dto);
    }

    @PatchMapping("/{id}")
    public AlistamentoResponseDTO atualizarParcialAlistamento(
            @PathVariable Integer id,
            @RequestBody AlistamentoRequestDTO dto) {

        return alistamentoService.atualizarParcial(id, dto);
    }

    @DeleteMapping("/{id}")
    public AlistamentoResponseDTO deletarAlistamento(
            @PathVariable Integer id) {

        return alistamentoService.deletarAlistamento(id);
    }
}
