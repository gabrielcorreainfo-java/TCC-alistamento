package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.DocumentoRequestDTO;
import com.example.tcc_alistamento.dto.DocumentoResponseDTO;
import com.example.tcc_alistamento.service.DocumentoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {
    private final DocumentoService documentoService;

    // Injeção de dependência da DocumentoService.
    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    // Endpoint para cadastrar um novo documento.
    // Recebe os dados em JSON e envia para a Service salvar.
    @PostMapping
    public DocumentoResponseDTO cadastrar(@RequestBody DocumentoRequestDTO dto) {
        return documentoService.salvar(dto);
    }

    // Endpoint para listar todos os documentos cadastrados.
    @GetMapping
    public List<DocumentoResponseDTO> listar() {
        return documentoService.listar();
    }

    // Endpoint para buscar documento pelo id.
    // PathVariable captura na URL o id.
    @GetMapping("/{id}")
    public DocumentoResponseDTO buscarPorId(@PathVariable Integer id) {
        return documentoService.buscarPorId(id);
    }

    // Endpoint para atualizar todo registro do documento.
    @PutMapping("/{id}")
    public DocumentoResponseDTO atualizarDocumento(@PathVariable Integer id,
                                                   @RequestBody DocumentoRequestDTO dto) {
        return documentoService.atualizar(id, dto);
    }

    // Endpoint para atualizar parcialmente um registro.
    @PatchMapping("/{id}")
    public DocumentoResponseDTO atualizarParcial(@PathVariable Integer id,
                                                 @RequestBody DocumentoRequestDTO dto) {
        return documentoService.atualizarParcial(id, dto);
    }

    // Endpoint para deletar documento.
    @DeleteMapping("/{id}")
    public DocumentoResponseDTO deletarDocumento(@PathVariable Integer id) {
        return documentoService.deletar(id);
    }
}
