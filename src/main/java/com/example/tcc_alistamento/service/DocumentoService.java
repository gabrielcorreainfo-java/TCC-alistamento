package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.DocumentoRequestDTO;
import com.example.tcc_alistamento.dto.DocumentoResponseDTO;
import com.example.tcc_alistamento.dto.DocumentoStatusRequestDTO;
import com.example.tcc_alistamento.exceptions.AlistamentoNotFoundException;
import com.example.tcc_alistamento.exceptions.DocumentoNotFoundException;
import com.example.tcc_alistamento.exceptions.TipoDeDocumentoNotFoundException;
import com.example.tcc_alistamento.exceptions.UsuarioNotFoundException;
import com.example.tcc_alistamento.model.Alistamento;
import com.example.tcc_alistamento.model.Documento;
import com.example.tcc_alistamento.model.TipoDocumento;
import com.example.tcc_alistamento.model.Usuario;
import com.example.tcc_alistamento.repository.AlistamentoRepository;
import com.example.tcc_alistamento.repository.DocumentoRepository;
import com.example.tcc_alistamento.repository.TipoDocumentoRepository;
import com.example.tcc_alistamento.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentoService {
    private final DocumentoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlistamentoRepository alistamentoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    public DocumentoService(
            DocumentoRepository documentoRepository,
            UsuarioRepository usuarioRepository,
            AlistamentoRepository alistamentoRepository,
            TipoDocumentoRepository tipoDocumentoRepository) {

        this.documentoRepository = documentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.alistamentoRepository = alistamentoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    public DocumentoResponseDTO salvar(DocumentoRequestDTO dto) {

        Usuario usuario = buscarUsuario(dto.idUsuario());

        Alistamento alistamento = buscarAlistamento(dto.idAlistamento());

        TipoDocumento tipoDocumento =
                buscarTipoDocumento(dto.idTipoDocumento());

        Documento documento = new Documento();

        documento.setNumeroDocumento(dto.numeroDocumento());
        documento.setNumeroFolha(dto.numeroFolha());
        documento.setNumeroLivro(dto.numeroLivro());
        documento.setDataEmissao(dto.dataEmissao());
        documento.setOrgaoEmissor(dto.orgaoEmissor());
        documento.setCidadeEmissao(dto.cidadeEmissao());
        documento.setEstadoEmissao(dto.estadoEmissao());
        documento.setNomeArquivo(dto.nomeArquivo());
        documento.setDataEnvio(dto.dataEnvio());

        documento.setUsuario(usuario);
        documento.setAlistamento(alistamento);
        documento.setTipoDocumento(tipoDocumento);

        Documento documentoSalvo = documentoRepository.save(documento);

        return new DocumentoResponseDTO(documentoSalvo);
    }

    public List<DocumentoResponseDTO> listar() {

        return documentoRepository.findAll()
                .stream()
                .map(DocumentoResponseDTO::new)
                .toList();
    }

    public DocumentoResponseDTO buscarPorId(Integer id) {

        Documento documento = buscarDocumento(id);

        return new DocumentoResponseDTO(documento);
    }

    public DocumentoResponseDTO atualizar(
            Integer id,
            DocumentoRequestDTO dto) {

        Documento documento = buscarDocumento(id);

        Usuario usuario = buscarUsuario(dto.idUsuario());

        Alistamento alistamento =
                buscarAlistamento(dto.idAlistamento());

        TipoDocumento tipoDocumento =
                buscarTipoDocumento(dto.idTipoDocumento());

        documento.setNumeroDocumento(dto.numeroDocumento());
        documento.setNumeroFolha(dto.numeroFolha());
        documento.setNumeroLivro(dto.numeroLivro());
        documento.setDataEmissao(dto.dataEmissao());
        documento.setOrgaoEmissor(dto.orgaoEmissor());
        documento.setCidadeEmissao(dto.cidadeEmissao());
        documento.setEstadoEmissao(dto.estadoEmissao());
        documento.setNomeArquivo(dto.nomeArquivo());
        documento.setDataEnvio(dto.dataEnvio());

        documento.setUsuario(usuario);
        documento.setAlistamento(alistamento);
        documento.setTipoDocumento(tipoDocumento);

        Documento documentoAtualizado =
                documentoRepository.save(documento);

        return new DocumentoResponseDTO(documentoAtualizado);
    }

    public DocumentoResponseDTO atualizarParcial(
            Integer id,
            DocumentoRequestDTO dto) {

        Documento documento = buscarDocumento(id);

        if (dto.numeroDocumento() != null) {
            documento.setNumeroDocumento(dto.numeroDocumento());
        }

        if (dto.numeroFolha() != null) {
            documento.setNumeroFolha(dto.numeroFolha());
        }

        if (dto.numeroLivro() != null) {
            documento.setNumeroLivro(dto.numeroLivro());
        }

        if (dto.dataEmissao() != null) {
            documento.setDataEmissao(dto.dataEmissao());
        }

        if (dto.orgaoEmissor() != null) {
            documento.setOrgaoEmissor(dto.orgaoEmissor());
        }

        if (dto.cidadeEmissao() != null) {
            documento.setCidadeEmissao(dto.cidadeEmissao());
        }

        if (dto.estadoEmissao() != null) {
            documento.setEstadoEmissao(dto.estadoEmissao());
        }

        if (dto.nomeArquivo() != null) {
            documento.setNomeArquivo(dto.nomeArquivo());
        }

        if (dto.dataEnvio() != null) {
            documento.setDataEnvio(dto.dataEnvio());
        }

        if (dto.idUsuario() != null) {
            documento.setUsuario(
                    buscarUsuario(dto.idUsuario())
            );
        }

        if (dto.idAlistamento() != null) {
            documento.setAlistamento(
                    buscarAlistamento(dto.idAlistamento())
            );
        }

        if (dto.idTipoDocumento() != null) {
            documento.setTipoDocumento(
                    buscarTipoDocumento(dto.idTipoDocumento())
            );
        }

        Documento documentoAtualizado =
                documentoRepository.save(documento);

        return new DocumentoResponseDTO(documentoAtualizado);
    }

    public DocumentoResponseDTO deletar(Integer id) {

        Documento documento = buscarDocumento(id);

        documentoRepository.delete(documento);

        return new DocumentoResponseDTO(documento);
    }

    public DocumentoResponseDTO atualizarStatus(Integer id, DocumentoStatusRequestDTO dto) {
        Documento documento = buscarDocumento(id);
        documento.setStatus(dto.status());

        Documento documentoAtualizado = documentoRepository.save(documento);
        return new DocumentoResponseDTO(documentoAtualizado);
    }

    private Documento buscarDocumento(Integer id) {

        return documentoRepository.findById(id)
                .orElseThrow(() ->
                        new DocumentoNotFoundException(
                                "Documento não encontrado"
                        )
                );
    }

    private Usuario buscarUsuario(Integer id) {

        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new UsuarioNotFoundException(
                                "Usuário não encontrado"
                        )
                );
    }

    private Alistamento buscarAlistamento(Integer id) {

        return alistamentoRepository.findById(id)
                .orElseThrow(() ->
                        new AlistamentoNotFoundException(
                                "Alistamento não encontrado"
                        )
                );
    }

    private TipoDocumento buscarTipoDocumento(Integer id) {

        return tipoDocumentoRepository.findById(id)
                .orElseThrow(() ->
                        new TipoDeDocumentoNotFoundException(
                                "Tipo de documento não encontrado"
                        )
                );
    }
}
