package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.DocumentoRequestDTO;
import com.example.tcc_alistamento.dto.DocumentoResponseDTO;
import com.example.tcc_alistamento.dto.DocumentoStatusRequestDTO;
import com.example.tcc_alistamento.exceptions.AlistamentoNotFoundException;
import com.example.tcc_alistamento.exceptions.DocumentoNotFoundException;
import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import com.example.tcc_alistamento.exceptions.TipoDeDocumentoNotFoundException;
import com.example.tcc_alistamento.exceptions.UsuarioNotFoundException;
import com.example.tcc_alistamento.infra.security.ControleAcesso;
import com.example.tcc_alistamento.model.Alistamento;
import com.example.tcc_alistamento.model.Documento;
import com.example.tcc_alistamento.model.TipoDocumento;
import com.example.tcc_alistamento.model.Usuario;
import com.example.tcc_alistamento.repository.AlistamentoRepository;
import com.example.tcc_alistamento.repository.DocumentoRepository;
import com.example.tcc_alistamento.repository.TipoDocumentoRepository;
import com.example.tcc_alistamento.repository.UsuarioRepository;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlistamentoRepository alistamentoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final ControleAcesso controleAcesso;
    private final ArquivoService arquivoService;
    private final RegrasCadastro regrasCadastro;

    public DocumentoService(
            DocumentoRepository documentoRepository,
            UsuarioRepository usuarioRepository,
            AlistamentoRepository alistamentoRepository,
            TipoDocumentoRepository tipoDocumentoRepository,
            ControleAcesso controleAcesso,
            ArquivoService arquivoService,
            RegrasCadastro regrasCadastro) {

        this.documentoRepository = documentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.alistamentoRepository = alistamentoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.controleAcesso = controleAcesso;
        this.arquivoService = arquivoService;
        this.regrasCadastro = regrasCadastro;
    }

    // Envio de documento pelo cidadão
    public DocumentoResponseDTO salvar(DocumentoRequestDTO dto) {

        Alistamento alistamento = buscarAlistamento(dto.idAlistamento());

        // O cidadão só envia documentos para o próprio alistamento
        controleAcesso.exigirDonoDoAlistamento(alistamento);

        TipoDocumento tipoDocumento = buscarTipoDocumento(dto.idTipoDocumento());
        validarDadosDoDocumento(dto, tipoDocumento);

        Documento documento = new Documento();
        documento.setNumeroDocumento(dto.numeroDocumento().trim());
        documento.setNumeroFolha(dto.numeroFolha());
        documento.setNumeroLivro(dto.numeroLivro());
        documento.setDataEmissao(dto.dataEmissao());
        documento.setOrgaoEmissor(dto.orgaoEmissor().trim());
        documento.setCidadeEmissao(dto.cidadeEmissao().trim());
        documento.setEstadoEmissao(dto.estadoEmissao().trim());
        documento.setNomeArquivo(dto.nomeArquivo());
        // A data de envio é a do servidor, não a enviada pelo cliente
        documento.setDataEnvio(LocalDateTime.now());
        // O dono do documento é o dono do alistamento (não o id que veio na requisição)
        documento.setUsuario(alistamento.getUsuario());
        documento.setAlistamento(alistamento);
        documento.setTipoDocumento(tipoDocumento);

        Documento documentoSalvo = documentoRepository.save(documento);
        return new DocumentoResponseDTO(documentoSalvo);
    }

    // Arquivo (PDF ou imagem) do documento: enviado logo depois dos dados
    public void salvarArquivo(Integer id, MultipartFile arquivo) {
        Documento documento = buscarDocumento(id);
        controleAcesso.exigirDonoDoAlistamento(documento.getAlistamento());

        String tipo = arquivoService.detectarTipo(arquivo);
        String nome = arquivoService.salvar(arquivo);

        // Se o cidadão reenviar, o arquivo antigo é apagado
        arquivoService.apagar(documento.getCaminhoArquivo());

        documento.setCaminhoArquivo(nome);
        documento.setTipoArquivo(tipo);
        documentoRepository.save(documento);
    }

    // Documento com arquivo (cidadão: só o próprio; médico e admin: todos)
    public Documento buscarComArquivo(Integer id) {
        Documento documento = buscarDocumento(id);
        controleAcesso.exigirDonoDoAlistamento(documento.getAlistamento());
        if (documento.getCaminhoArquivo() == null) {
            throw new RegraDeNegocioException(HttpStatus.NOT_FOUND, "Este documento não possui arquivo.");
        }
        return documento;
    }

    public Resource carregarArquivo(Documento documento) {
        return arquivoService.carregar(documento.getCaminhoArquivo());
    }

    // Lista completa: só ADMIN e MEDICO (regra no SecurityConfiguration)
    public List<DocumentoResponseDTO> listar() {
        return documentoRepository.findAll()
                .stream()
                .map(DocumentoResponseDTO::new)
                .toList();
    }

    public DocumentoResponseDTO buscarPorId(Integer id) {
        Documento documento = buscarDocumento(id);
        controleAcesso.exigirDonoDoAlistamento(documento.getAlistamento());
        return new DocumentoResponseDTO(documento);
    }

    // Edição completa: só ADMIN
    public DocumentoResponseDTO atualizar(Integer id, DocumentoRequestDTO dto) {

        Documento documento = buscarDocumento(id);

        Usuario usuario = buscarUsuario(dto.idUsuario());
        Alistamento alistamento = buscarAlistamento(dto.idAlistamento());
        TipoDocumento tipoDocumento = buscarTipoDocumento(dto.idTipoDocumento());

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

        return new DocumentoResponseDTO(documentoRepository.save(documento));
    }

    // Edição parcial: só ADMIN
    public DocumentoResponseDTO atualizarParcial(Integer id, DocumentoRequestDTO dto) {

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
            documento.setUsuario(buscarUsuario(dto.idUsuario()));
        }
        if (dto.idAlistamento() != null) {
            documento.setAlistamento(buscarAlistamento(dto.idAlistamento()));
        }
        if (dto.idTipoDocumento() != null) {
            documento.setTipoDocumento(buscarTipoDocumento(dto.idTipoDocumento()));
        }

        return new DocumentoResponseDTO(documentoRepository.save(documento));
    }

    // Cidadão (só o próprio documento) ou administrador
    public DocumentoResponseDTO deletar(Integer id) {

        Documento documento = buscarDocumento(id);

        controleAcesso.exigirDonoDoAlistamento(documento.getAlistamento());
        documentoRepository.delete(documento);
        arquivoService.apagar(documento.getCaminhoArquivo());

        return new DocumentoResponseDTO(documento);
    }

    // Aprovar ou reprovar o documento: só ADMIN
    public DocumentoResponseDTO atualizarStatus(Integer id, DocumentoStatusRequestDTO dto) {
        Documento documento = buscarDocumento(id);
        documento.setStatus(dto.status());
        return new DocumentoResponseDTO(documentoRepository.save(documento));
    }

    // ----- Métodos auxiliares -----

    /*
    Validação dos dados do documento.
    O RG tem formato próprio; para os outros documentos (certidão, comprovante...)
    o formato varia muito, então conferimos só o essencial.
     */
    private void validarDadosDoDocumento(DocumentoRequestDTO dto, TipoDocumento tipo) {
        if (vazio(dto.numeroDocumento()) || vazio(dto.orgaoEmissor())
                || vazio(dto.cidadeEmissao()) || vazio(dto.estadoEmissao())) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST,
                    "Preencha número, órgão emissor, cidade e estado de emissão.");
        }
        if (dto.dataEmissao() == null || dto.dataEmissao().isAfter(LocalDate.now())) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "Data de emissão inválida.");
        }
        if (tipo.getNomeTipo() != null && tipo.getNomeTipo().trim().equalsIgnoreCase("RG")) {
            regrasCadastro.validarRg(dto.numeroDocumento());
        }
    }

    private boolean vazio(String texto) {
        return texto == null || texto.isBlank();
    }

    private Documento buscarDocumento(Integer id) {
        return documentoRepository.findById(id)
                .orElseThrow(() -> new DocumentoNotFoundException("Documento não encontrado"));
    }

    private Usuario buscarUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));
    }

    private Alistamento buscarAlistamento(Integer id) {
        return alistamentoRepository.findById(id)
                .orElseThrow(() -> new AlistamentoNotFoundException("Alistamento não encontrado"));
    }

    private TipoDocumento buscarTipoDocumento(Integer id) {
        return tipoDocumentoRepository.findById(id)
                .orElseThrow(() -> new TipoDeDocumentoNotFoundException("Tipo de documento não encontrado"));
    }
}