package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.TipoDocumentoRequestDTO;
import com.example.tcc_alistamento.dto.TipoDocumentoResponseDTO;
import com.example.tcc_alistamento.service.TipoDocumentoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos-documento")
public class TipoDocumentoController {
    private final TipoDocumentoService tipoDocumentoService;

    public TipoDocumentoController(TipoDocumentoService tipoDocumentoService) {
        this.tipoDocumentoService = tipoDocumentoService;
    }

    @PostMapping
    public TipoDocumentoResponseDTO salvar(@RequestBody TipoDocumentoRequestDTO dto){
        return tipoDocumentoService.salvar(dto);
    }

    @GetMapping
    public List<TipoDocumentoResponseDTO> listar(){
        return tipoDocumentoService.listar();
    }

    @GetMapping("/{id}")
    public TipoDocumentoResponseDTO buscarPorid(@PathVariable Integer id){
        return tipoDocumentoService.bucarPorId(id);
    }

    @PutMapping("/{id}")
    public TipoDocumentoResponseDTO atualizar(@PathVariable Integer id, @RequestBody TipoDocumentoRequestDTO dto){
        return tipoDocumentoService.atualizar(id, dto);
    }

    @PatchMapping("/{id}")
    public TipoDocumentoResponseDTO atualizarParcial(@PathVariable Integer id, @RequestBody TipoDocumentoRequestDTO dto){
        return tipoDocumentoService.atualizarParcial(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id){
        tipoDocumentoService.deletar(id);
    }
}
