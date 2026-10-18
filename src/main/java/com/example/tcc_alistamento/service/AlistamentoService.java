package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.AlistamentoCompletoResponseDTO;
import com.example.tcc_alistamento.dto.AlistamentoRequestDTO;
import com.example.tcc_alistamento.dto.AlistamentoResponseDTO;
import com.example.tcc_alistamento.dto.DocumentoResponseDTO;
import com.example.tcc_alistamento.exceptions.AdministradorNotFoundException;
import com.example.tcc_alistamento.exceptions.AlistamentoNotFoundException;
import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import com.example.tcc_alistamento.exceptions.UsuarioNotFoundException;
import com.example.tcc_alistamento.infra.security.ControleAcesso;
import com.example.tcc_alistamento.model.Administrador;
import com.example.tcc_alistamento.model.Alistamento;
import com.example.tcc_alistamento.model.Usuario;
import com.example.tcc_alistamento.repository.AdministradorRepository;
import com.example.tcc_alistamento.repository.AlistamentoRepository;
import com.example.tcc_alistamento.repository.DocumentoRepository;
import com.example.tcc_alistamento.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class AlistamentoService {

    // Status usados pelo front (enum StatusAlistamento)
    public static final String CADASTRO_INCOMPLETO = "Cadastro incompleto";
    public static final String AGUARDANDO_DOCUMENTOS = "Aguardando documentos";
    public static final String EM_ANALISE = "Em análise";
    public static final String DOCUMENTOS_REPROVADOS = "Documentos reprovados";
    public static final String AVALIACAO_AGENDADA = "Avaliação médica agendada";
    public static final String AVALIACAO_CONCLUIDA = "Avaliação médica concluída";
    public static final String APROVADO = "Aprovado";
    public static final String REPROVADO = "Reprovado";

    // De onde o cidadão pode mandar o processo para análise (ao enviar documentos)
    private static final Set<String> ANTES_DA_ANALISE =
            Set.of(CADASTRO_INCOMPLETO, AGUARDANDO_DOCUMENTOS, DOCUMENTOS_REPROVADOS);

    private final AlistamentoRepository alistamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AdministradorRepository administradorRepository;
    private final DocumentoRepository documentoRepository;
    private final RegrasCadastro regrasCadastro;
    private final ControleAcesso controleAcesso;

    public AlistamentoService(AlistamentoRepository alistamentoRepository,
                              UsuarioRepository usuarioRepository,
                              AdministradorRepository administradorRepository,
                              DocumentoRepository documentoRepository,
                              RegrasCadastro regrasCadastro,
                              ControleAcesso controleAcesso) {
        this.alistamentoRepository = alistamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.administradorRepository = administradorRepository;
        this.documentoRepository = documentoRepository;
        this.regrasCadastro = regrasCadastro;
        this.controleAcesso = controleAcesso;
    }

    // Abertura do alistamento (feita pelo próprio cidadão)
    public AlistamentoResponseDTO salvar(AlistamentoRequestDTO dto) {

        // O cidadão só abre alistamento para ele mesmo
        controleAcesso.exigirDonoDoUsuario(dto.idUsuario());

        Usuario usuario = buscarUsuario(dto.idUsuario());

        if (usuario.getAlistamento() != null) {
            throw new RegraDeNegocioException(HttpStatus.CONFLICT, "Este cidadão já possui um alistamento.");
        }

        // Regra do servidor: idade, sexo (modalidade) e nacionalidade
        regrasCadastro.validarElegibilidade(usuario.getDataNascimento(), usuario.getSexo(), usuario.getNacionalidade());

        Alistamento alistamento = new Alistamento();
        // A data e o status inicial são definidos pela API, não pelo cliente
        alistamento.setDataAlistamento(LocalDate.now());
        alistamento.setStatus(AGUARDANDO_DOCUMENTOS);
        alistamento.setUsuario(usuario);

        // O administrador é opcional: o responsável é registrado quando ele analisa
        if (dto.idAdministrador() != null) {
            alistamento.setAdministrador(buscarAdministrador(dto.idAdministrador()));
        }

        return new AlistamentoResponseDTO(alistamentoRepository.save(alistamento));
    }

    // Lista completa: só ADMIN e MEDICO (regra no SecurityConfiguration)
    public List<AlistamentoResponseDTO> listar() {
        return alistamentoRepository.findAll()
                .stream()
                .map(AlistamentoResponseDTO::new)
                .toList();
    }

    public AlistamentoResponseDTO buscarPorId(Integer id) {
        Alistamento alistamento = buscarAlistamento(id);
        controleAcesso.exigirDonoDoAlistamento(alistamento);
        return new AlistamentoResponseDTO(alistamento);
    }

    // Edição completa: só ADMIN
    public AlistamentoResponseDTO atualizar(Integer id, AlistamentoRequestDTO dto) {
        Alistamento alistamentoExistente = buscarAlistamento(id);

        if (dto.status() != null) {
            validarParecerFinal(alistamentoExistente, dto.status());
            alistamentoExistente.setStatus(dto.status());
        }
        if (dto.dataAlistamento() != null) {
            alistamentoExistente.setDataAlistamento(dto.dataAlistamento());
        }
        if (dto.idUsuario() != null) {
            alistamentoExistente.setUsuario(buscarUsuario(dto.idUsuario()));
        }
        if (dto.idAdministrador() != null) {
            alistamentoExistente.setAdministrador(buscarAdministrador(dto.idAdministrador()));
        }

        return new AlistamentoResponseDTO(alistamentoRepository.save(alistamentoExistente));
    }

    /*
    Atualização do status. Cada perfil só pode fazer a mudança que é dele:
    - Cidadão: mandar para "Em análise" depois de enviar os documentos.
    - Médico:  marcar "Avaliação médica concluída" depois de avaliar.
    - Admin:   todas as outras, mas o parecer final exige a avaliação médica.
    Assim ninguém altera o próprio resultado com uma requisição manual.
     */
    public AlistamentoResponseDTO atualizarParcial(Integer id, AlistamentoRequestDTO dto) {

        Alistamento alistamento = buscarAlistamento(id);

        if (controleAcesso.ehCidadao()) {
            controleAcesso.exigirDonoDoAlistamento(alistamento);
            exigirSomenteStatus(dto);

            if (!EM_ANALISE.equals(dto.status()) || !ANTES_DA_ANALISE.contains(alistamento.getStatus())) {
                throw controleAcesso.semPermissao();
            }
            alistamento.setStatus(EM_ANALISE);
            return new AlistamentoResponseDTO(alistamentoRepository.save(alistamento));
        }

        if (controleAcesso.ehMedico()) {
            exigirSomenteStatus(dto);

            if (!AVALIACAO_CONCLUIDA.equals(dto.status()) || !AVALIACAO_AGENDADA.equals(alistamento.getStatus())) {
                throw controleAcesso.semPermissao();
            }
            alistamento.setStatus(AVALIACAO_CONCLUIDA);
            return new AlistamentoResponseDTO(alistamentoRepository.save(alistamento));
        }

        // Administrador
        if (dto.status() != null) {
            validarParecerFinal(alistamento, dto.status());
            alistamento.setStatus(dto.status());
        }
        if (dto.dataAlistamento() != null) {
            alistamento.setDataAlistamento(dto.dataAlistamento());
        }
        if (dto.idUsuario() != null) {
            alistamento.setUsuario(buscarUsuario(dto.idUsuario()));
        }
        if (dto.idAdministrador() != null) {
            alistamento.setAdministrador(buscarAdministrador(dto.idAdministrador()));
        }

        return new AlistamentoResponseDTO(alistamentoRepository.save(alistamento));
    }

    public AlistamentoResponseDTO deletarAlistamento(Integer id) {
        Alistamento alistamentoExistente = buscarAlistamento(id);
        alistamentoRepository.delete(alistamentoExistente);
        return new AlistamentoResponseDTO(alistamentoExistente);
    }

    // Visão completa do processo (cidadão: só o próprio; médico e admin: todos)
    public AlistamentoCompletoResponseDTO buscarCompleto(Integer id) {
        Alistamento alistamento = buscarAlistamento(id);
        controleAcesso.exigirDonoDoAlistamento(alistamento);

        List<DocumentoResponseDTO> documentos = documentoRepository
                .findByAlistamentoId(id)
                .stream()
                .map(DocumentoResponseDTO::new)
                .toList();

        return new AlistamentoCompletoResponseDTO(alistamento, documentos);
    }

    // ----- Métodos auxiliares -----

    // O parecer final (Aprovado/Reprovado) só pode ser dado depois da avaliação médica
    private void validarParecerFinal(Alistamento alistamento, String novoStatus) {
        boolean parecerFinal = APROVADO.equals(novoStatus) || REPROVADO.equals(novoStatus);
        if (parecerFinal && alistamento.getAvaliacaoMedica() == null) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST,
                    "O parecer final só pode ser emitido depois da avaliação médica.");
        }
    }

    // Cidadão e médico só podem mudar o status, nada mais
    private void exigirSomenteStatus(AlistamentoRequestDTO dto) {
        if (dto.status() == null || dto.idUsuario() != null
                || dto.idAdministrador() != null || dto.dataAlistamento() != null) {
            throw controleAcesso.semPermissao();
        }
    }

    private Usuario buscarUsuario(Integer id) {
        if (id == null) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "Informe o cidadão.");
        }
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));
    }

    private Administrador buscarAdministrador(Integer id) {
        return administradorRepository.findById(id)
                .orElseThrow(() -> new AdministradorNotFoundException("Administrador não encontrado"));
    }

    private Alistamento buscarAlistamento(Integer id) {
        return alistamentoRepository.findById(id)
                .orElseThrow(() -> new AlistamentoNotFoundException("Alistamento não encontrado"));
    }
}