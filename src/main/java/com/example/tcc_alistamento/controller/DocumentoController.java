package com.example.tcc_alistamento.controller;

import com.example.tcc_alistamento.dto.DocumentoRequestDTO;
import com.example.tcc_alistamento.dto.DocumentoResponseDTO;
import com.example.tcc_alistamento.dto.DocumentoStatusRequestDTO;
import com.example.tcc_alistamento.model.Documento;
import com.example.tcc_alistamento.service.DocumentoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;

    // Injeção de dependência da DocumentoService.
    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    // Cadastra os dados do documento (JSON).
    @PostMapping
    public DocumentoResponseDTO cadastrar(@RequestBody DocumentoRequestDTO dto) {
        return documentoService.salvar(dto);
    }

    // Lista todos os documentos: só ADMIN e MEDICO.
    @GetMapping
    public List<DocumentoResponseDTO> listar() {
        return documentoService.listar();
    }

    // Busca documento pelo id (cidadão: só o próprio).
    @GetMapping("/{id}")
    public DocumentoResponseDTO buscarPorId(@PathVariable Integer id) {
        return documentoService.buscarPorId(id);
    }

    // Atualiza todo o registro do documento: só ADMIN.
    @PutMapping("/{id}")
    public DocumentoResponseDTO atualizarDocumento(@PathVariable Integer id, @RequestBody DocumentoRequestDTO dto) {
        return documentoService.atualizar(id, dto);
    }

    // Atualiza parcialmente: só ADMIN.
    @PatchMapping("/{id}")
    public DocumentoResponseDTO atualizarParcial(@PathVariable Integer id, @RequestBody DocumentoRequestDTO dto) {
        return documentoService.atualizarParcial(id, dto);
    }

    // Deleta o documento (cidadão: só o próprio; ou ADMIN).
    @DeleteMapping("/{id}")
    public DocumentoResponseDTO deletarDocumento(@PathVariable Integer id) {
        return documentoService.deletar(id);
    }

    // Aprova ou reprova o documento: só ADMIN.
    @PatchMapping("/{id}/status")
    public DocumentoResponseDTO atualizarStatus(@PathVariable Integer id, @RequestBody DocumentoStatusRequestDTO dto) {
        return documentoService.atualizarStatus(id, dto);
    }

    // Envio do arquivo (PDF ou imagem), em formato multipart, no campo "arquivo"
    @PostMapping(value = "/{id}/arquivo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> enviarArquivo(@PathVariable Integer id,
                                              @RequestParam("arquivo") MultipartFile arquivo) {
        documentoService.salvarArquivo(id, arquivo);
        return ResponseEntity.noContent().build();
    }

    // Download do arquivo para exibir na tela (cidadão dono, médico ou administrador)
    @GetMapping("/{id}/arquivo")
    public ResponseEntity<Resource> baixarArquivo(@PathVariable Integer id) {
        Documento documento = documentoService.buscarComArquivo(id);
        Resource arquivo = documentoService.carregarArquivo(documento);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(documento.getTipoArquivo()))
                // "inline": o navegador mostra o arquivo em vez de baixar
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(arquivo);
    }
}