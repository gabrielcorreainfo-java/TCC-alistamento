package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.AdministradorAlistamentosResponseDTO;
import com.example.tcc_alistamento.dto.AdministradorRequestDTO;
import com.example.tcc_alistamento.dto.AdministradorResponseDTO;
import com.example.tcc_alistamento.service.AdministradorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/administrador")
public class AdministradorController {

    private final AdministradorService administradorService;

    // O login do administrador é feito pelo /auth/login (login único)
    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @PostMapping
    public AdministradorResponseDTO cadastrar(@RequestBody AdministradorRequestDTO dto) {
        return administradorService.salvar(dto);
    }

    @GetMapping
    public List<AdministradorResponseDTO> listar() {
        return administradorService.listar();
    }

    @GetMapping("/{id}")
    public AdministradorResponseDTO buscarPorId(@PathVariable Integer id) {
        return administradorService.buscarPorId(id);
    }

    @GetMapping("/{id}/alistamentos")
    public AdministradorAlistamentosResponseDTO buscarAlistamentos(@PathVariable Integer id) {
        return administradorService.buscarAlistamentos(id);
    }

    @PutMapping("/{id}")
    public AdministradorResponseDTO atualizar(@PathVariable Integer id, @RequestBody AdministradorRequestDTO dto) {
        return administradorService.atualizar(id, dto);
    }

    @PatchMapping("/{id}")
    public AdministradorResponseDTO atualizarParcial(@PathVariable Integer id, @RequestBody AdministradorRequestDTO dto) {
        return administradorService.atualizarParcial(id, dto);
    }

    @DeleteMapping("/{id}")
    public AdministradorResponseDTO deletar(@PathVariable Integer id) {
        return administradorService.deletar(id);
    }
}