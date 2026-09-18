package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.TipoDocumentoRequestDTO;
import com.example.tcc_alistamento.dto.TipoDocumentoResponseDTO;
import com.example.tcc_alistamento.exceptions.TipoDeDocumentoNotFoundException;
import com.example.tcc_alistamento.model.TipoDocumento;
import com.example.tcc_alistamento.repository.TipoDocumentoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoDocumentoService {
    private final TipoDocumentoRepository tipoDocumentoRepository;

    public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    public TipoDocumentoResponseDTO salvar(TipoDocumentoRequestDTO dto){
        TipoDocumento tipoDocumento = new TipoDocumento();
        BeanUtils.copyProperties(dto,tipoDocumento);
        TipoDocumento tipoDocumentoSalvo = tipoDocumentoRepository.save(tipoDocumento);
        return new TipoDocumentoResponseDTO(tipoDocumentoSalvo);
    }

    public List<TipoDocumentoResponseDTO> listar(){
        return tipoDocumentoRepository.findAll().stream()
                .map(TipoDocumentoResponseDTO::new)
                .toList();
    }

    public TipoDocumentoResponseDTO bucarPorId(Integer id){
        TipoDocumento tipoExiste = buscarTipoDeDocumentoPorId(id);
        return new TipoDocumentoResponseDTO(tipoExiste);
    }

    public TipoDocumento buscarTipoDeDocumentoPorId(Integer id ){
        return tipoDocumentoRepository.findById(id).orElseThrow(()-> new TipoDeDocumentoNotFoundException("Tipo de Documento não encontrado"));
    }

    public TipoDocumentoResponseDTO atualizar(Integer id, TipoDocumentoRequestDTO dto){
        TipoDocumento tipoExiste = buscarTipoDeDocumentoPorId(id);
        BeanUtils.copyProperties(dto,tipoExiste);
        TipoDocumento tipoAtualizado = tipoDocumentoRepository.save(tipoExiste);
        return new TipoDocumentoResponseDTO(tipoAtualizado);
    }

    public TipoDocumentoResponseDTO atualizarParcial(Integer id, TipoDocumentoRequestDTO dto){
        TipoDocumento tipoDocumentoExistente = buscarTipoDeDocumentoPorId(id);

        if (dto.nomeTipo() != null) {
            tipoDocumentoExistente.setNomeTipo(dto.nomeTipo());
        }
        if (dto.descricao() != null) {
            tipoDocumentoExistente.setDescricao(dto.descricao());
        }

        TipoDocumento tipoAtualizado = tipoDocumentoRepository.save(tipoDocumentoExistente);
        return new TipoDocumentoResponseDTO(tipoAtualizado);
    }

    public void deletar(Integer id){
        TipoDocumento tipoDocumentoExistente = buscarTipoDeDocumentoPorId(id);
        tipoDocumentoRepository.delete(tipoDocumentoExistente);
    }
}
