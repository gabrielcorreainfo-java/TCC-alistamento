package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.AgendamentoRequestDTO;
import com.example.tcc_alistamento.dto.AgendamentoResponseDTO;
import com.example.tcc_alistamento.service.AgendamentoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {
    private final AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public AgendamentoResponseDTO cadastrar(@RequestBody AgendamentoRequestDTO dto) {

        return agendamentoService.salvar(dto);
    }

    @GetMapping
    public List<AgendamentoResponseDTO> listar() {

        return agendamentoService.listar();
    }

    @GetMapping("/{id}")
    public AgendamentoResponseDTO buscarPorId(
            @PathVariable Integer id) {

        return agendamentoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public AgendamentoResponseDTO atualizarAgendamento(
            @PathVariable Integer id,
            @RequestBody AgendamentoRequestDTO dto) {

        return agendamentoService.atualizar(id, dto);
    }

    @PatchMapping("/{id}")
    public AgendamentoResponseDTO atualizarParcial(
            @PathVariable Integer id,
            @RequestBody AgendamentoRequestDTO dto) {

        return agendamentoService.atualizarParcial(id, dto);
    }

    @DeleteMapping("/{id}")
    public AgendamentoResponseDTO deletarAgendamento(
            @PathVariable Integer id) {

        return agendamentoService.deletar(id);
    }

    @PatchMapping("/{id}/confirmar")
    public AgendamentoResponseDTO confirmarPresenca(@PathVariable Integer id) {
        return agendamentoService.confirmarPresenca(id);
    }
}
