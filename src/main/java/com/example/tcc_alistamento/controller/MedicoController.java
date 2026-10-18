package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.MedicoRequestDTO;
import com.example.tcc_alistamento.dto.MedicoResponseDTO;
import com.example.tcc_alistamento.service.MedicoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medico")
public class MedicoController {

    // Service responsável pelas regras de negócio do médico.
    private final MedicoService medicoService;

    // O login do médico é feito pelo /auth/login (login único)
    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    // Cadastra um novo médico (só ADMIN).
    @PostMapping
    public MedicoResponseDTO cadastrar(@RequestBody MedicoRequestDTO dto) {
        return medicoService.salvar(dto);
    }

    // Lista todos os médicos.
    @GetMapping
    public List<MedicoResponseDTO> listar() {
        return medicoService.listar();
    }

    // Busca médico pelo id.
    @GetMapping("/{id}")
    public MedicoResponseDTO buscarPorId(@PathVariable Integer id) {
        return medicoService.buscarPorId(id);
    }

    // Atualiza todo o registro do médico (só ADMIN).
    @PutMapping("/{id}")
    public MedicoResponseDTO atualizarMedico(@PathVariable Integer id, @RequestBody MedicoRequestDTO dto) {
        return medicoService.atualizar(id, dto);
    }

    // Atualiza parcialmente (só ADMIN).
    @PatchMapping("/{id}")
    public MedicoResponseDTO atualizarParcial(@PathVariable Integer id, @RequestBody MedicoRequestDTO dto) {
        return medicoService.atualizarParcial(id, dto);
    }

    // Deleta o médico (só ADMIN).
    @DeleteMapping("/{id}")
    public MedicoResponseDTO deletarMedico(@PathVariable Integer id) {
        return medicoService.deletarMedico(id);
    }
}