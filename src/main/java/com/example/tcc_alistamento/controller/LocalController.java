package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.LocalRequestDTO;
import com.example.tcc_alistamento.dto.LocalResponseDTO;
import com.example.tcc_alistamento.dto.UsuarioResponseDTO;
import com.example.tcc_alistamento.service.LocalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/local")
public class LocalController {
    private final LocalService localService;

    public LocalController(LocalService localService) {
        this.localService = localService;
    }

    @PostMapping
    public LocalResponseDTO salvar(@RequestBody LocalRequestDTO dto){
        return localService.salvar(dto);
    }

    @GetMapping
    public List<LocalResponseDTO> listar(){
        return localService.listar();
    }

    @GetMapping("/{id}")
    public LocalResponseDTO buscarPorId(@PathVariable Integer id){
        return localService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public LocalResponseDTO atualizarLocal(@PathVariable Integer id, @RequestBody LocalRequestDTO dto){
        return localService.atualizarLocal(id,dto);
    }

    @PatchMapping("/{id}")
    public LocalResponseDTO atualizarParcial(@PathVariable Integer id, @RequestBody LocalRequestDTO dto){
        return localService.atualizarParcial(id,dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id){
        localService.deletarLocal(id);
    }
}
