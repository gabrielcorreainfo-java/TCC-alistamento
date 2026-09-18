package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.AvaliacaoMedicaRequestDTO;
import com.example.tcc_alistamento.dto.AvaliacaoMedicaResponseDTO;
import com.example.tcc_alistamento.service.AvaliacaoMedicaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes-medicas")
public class AvaliacaoMedicaController {
    private final AvaliacaoMedicaService avaliacaoMedicaService;

    public AvaliacaoMedicaController(AvaliacaoMedicaService avaliacaoMedicaService) {
        this.avaliacaoMedicaService = avaliacaoMedicaService;
    }

    @PostMapping
    public AvaliacaoMedicaResponseDTO cadastrar(@RequestBody AvaliacaoMedicaRequestDTO dto) {
        return avaliacaoMedicaService.salvar(dto);
    }

    @GetMapping
    public List<AvaliacaoMedicaResponseDTO> listar() {
        return avaliacaoMedicaService.listar();
    }

    @GetMapping("/{id}")
    public AvaliacaoMedicaResponseDTO buscarPorId(@PathVariable Integer id) {
        return avaliacaoMedicaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public AvaliacaoMedicaResponseDTO atualizarAvaliacao(@PathVariable Integer id, @RequestBody AvaliacaoMedicaRequestDTO dto) {
        return avaliacaoMedicaService.atualizar(id, dto);
    }

    @PatchMapping("/{id}")
    public AvaliacaoMedicaResponseDTO atualizarParcial(@PathVariable Integer id, @RequestBody AvaliacaoMedicaRequestDTO dto) {
        return avaliacaoMedicaService.atualizarParcial(id, dto);
    }

    @DeleteMapping("/{id}")
    public AvaliacaoMedicaResponseDTO deletarAvaliacao(@PathVariable Integer id) {
        return avaliacaoMedicaService.deletar(id);
    }
}
