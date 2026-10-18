package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.EnderecoResponseDTO;
import com.example.tcc_alistamento.service.CepService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cep")
public class CepController {
    private final CepService cepService;

    public CepController(CepService cepService) {
        this.cepService = cepService;
    }

    @GetMapping("/{cep}")
    public EnderecoResponseDTO buscar(@PathVariable String cep) {
        return cepService.buscar(cep);
    }
}
