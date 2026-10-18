package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.AlistamentoResponseDTO;
import com.example.tcc_alistamento.dto.EnderecoResponseDTO;
import com.example.tcc_alistamento.dto.UsuarioRequestDTO;
import com.example.tcc_alistamento.dto.UsuarioResponseDTO;
import com.example.tcc_alistamento.exceptions.AlistamentoNotFoundException;
import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import com.example.tcc_alistamento.exceptions.UsuarioNotFoundException;
import com.example.tcc_alistamento.infra.security.ControleAcesso;
import com.example.tcc_alistamento.model.Usuario;
import com.example.tcc_alistamento.repository.AdministradorRepository;
import com.example.tcc_alistamento.repository.MedicoRepository;
import com.example.tcc_alistamento.repository.UsuarioRepository;
import com.example.tcc_alistamento.security.Criptografia;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AdministradorRepository administradorRepository;
    private final MedicoRepository medicoRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegrasCadastro regrasCadastro;
    private final CepService cepService;
    private final ControleAcesso controleAcesso;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          AdministradorRepository administradorRepository,
                          MedicoRepository medicoRepository,
                          PasswordEncoder passwordEncoder,
                          RegrasCadastro regrasCadastro,
                          CepService cepService,
                          ControleAcesso controleAcesso) {
        this.usuarioRepository = usuarioRepository;
        this.administradorRepository = administradorRepository;
        this.medicoRepository = medicoRepository;
        this.passwordEncoder = passwordEncoder;
        this.regrasCadastro = regrasCadastro;
        this.cepService = cepService;
        this.controleAcesso = controleAcesso;
    }

    // ----- Cadastro (rota pública) -----

    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto) {

        // 1. Regras do alistamento e da LGPD
        regrasCadastro.validarElegibilidade(dto.dataNascimento(), dto.sexo(), dto.nacionalidade());
        regrasCadastro.validarConsentimento(dto.consentimentoLgpd());

        if (dto.senha() == null || dto.senha().length() < 6) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "A senha deve ter pelo menos 6 caracteres.");
        }

        // 2. Formato dos documentos e do endereço
        String cpf = regrasCadastro.normalizarCpf(dto.cpf());
        String rg = regrasCadastro.validarRg(dto.rg());
        regrasCadastro.validarUf(dto.uf());

        // 3. Duplicidades: e-mail único entre os 3 perfis e CPF único
        validarEmailDisponivel(dto.email());
        if (usuarioRepository.existsByCpfHash(Criptografia.hash(cpf))) {
            throw new RegraDeNegocioException(HttpStatus.CONFLICT, "Este CPF já está cadastrado.");
        }

        Usuario usuario = new Usuario();
        copiarDados(dto, usuario);
        usuario.setCpf(cpf);
        usuario.setRg(rg);
        preencherEndereco(dto, usuario);

        // 4. Senha com BCrypt e registro do consentimento (LGPD)
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setConsentimentoLgpd(true);
        usuario.setDataConsentimento(LocalDateTime.now());

        usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(usuario);
    }

    // ----- Consultas -----

    // Lista completa: só ADMIN e MEDICO chegam aqui (regra no SecurityConfiguration)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::new)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Integer id) {
        // LGPD: o cidadão só consulta o próprio cadastro
        controleAcesso.exigirDonoDoUsuario(id);
        return new UsuarioResponseDTO(buscarUsuarioPorId(id));
    }

    public Usuario buscarUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));
    }

    public AlistamentoResponseDTO buscarAlistamentoDoUsuario(Integer idUsuario) {
        controleAcesso.exigirDonoDoUsuario(idUsuario);

        Usuario usuario = buscarUsuarioPorId(idUsuario);
        if (usuario.getAlistamento() == null) {
            throw new AlistamentoNotFoundException("Usuário ainda não possui alistamento");
        }
        return new AlistamentoResponseDTO(usuario.getAlistamento());
    }

    // ----- Edição (só ADMIN, regra no SecurityConfiguration) -----

    public UsuarioResponseDTO atualizarUsuario(Integer id, UsuarioRequestDTO dto) {

        Usuario usuarioExistente = buscarUsuarioPorId(id);

        String cpf = regrasCadastro.normalizarCpf(dto.cpf());
        String rg = regrasCadastro.validarRg(dto.rg());
        regrasCadastro.validarUf(dto.uf());

        if (usuarioRepository.existsByCpfHashAndIdNot(Criptografia.hash(cpf), id)) {
            throw new RegraDeNegocioException(HttpStatus.CONFLICT, "Este CPF já está cadastrado.");
        }
        if (!usuarioExistente.getEmail().equalsIgnoreCase(dto.email())) {
            validarEmailDisponivel(dto.email());
        }

        copiarDados(dto, usuarioExistente);
        usuarioExistente.setCpf(cpf);
        usuarioExistente.setRg(rg);
        preencherEndereco(dto, usuarioExistente);

        // Sexo e nacionalidade só mudam se vierem preenchidos e válidos
        if (dto.sexo() != null && !dto.sexo().isBlank()) {
            regrasCadastro.validarSexo(dto.sexo());
            usuarioExistente.setSexo(dto.sexo());
        }
        if (dto.nacionalidade() != null && !dto.nacionalidade().isBlank()) {
            regrasCadastro.validarNacionalidade(dto.nacionalidade());
            usuarioExistente.setNacionalidade(dto.nacionalidade());
        }

        // Só troca a senha se uma nova foi enviada (é assim que o admin redefine a senha)
        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(dto.senha()));
        }

        return new UsuarioResponseDTO(usuarioRepository.save(usuarioExistente));
    }

    public UsuarioResponseDTO atualizarParcial(Integer id, UsuarioRequestDTO dto) {
        Usuario usuarioExistente = buscarUsuarioPorId(id);

        if (dto.nome() != null) {
            usuarioExistente.setNome(dto.nome());
        }
        if (dto.telefone() != null) {
            usuarioExistente.setTelefone(dto.telefone());
        }
        if (dto.email() != null && !dto.email().equalsIgnoreCase(usuarioExistente.getEmail())) {
            validarEmailDisponivel(dto.email());
            usuarioExistente.setEmail(dto.email());
        }
        if (dto.cpf() != null) {
            String cpf = regrasCadastro.normalizarCpf(dto.cpf());
            if (usuarioRepository.existsByCpfHashAndIdNot(Criptografia.hash(cpf), id)) {
                throw new RegraDeNegocioException(HttpStatus.CONFLICT, "Este CPF já está cadastrado.");
            }
            usuarioExistente.setCpf(cpf);
        }

        usuarioRepository.save(usuarioExistente);
        return new UsuarioResponseDTO(usuarioExistente);
    }

    public UsuarioResponseDTO deletarUsuario(Integer id) {
        Usuario usuarioExistente = buscarUsuarioPorId(id);
        usuarioRepository.delete(usuarioExistente);
        return new UsuarioResponseDTO(usuarioExistente);
    }

    // ----- Métodos auxiliares -----

    // Copia os dados simples. CPF, RG, CEP, senha e consentimento são tratados à parte.
    private void copiarDados(UsuarioRequestDTO dto, Usuario usuario) {
        usuario.setNome(dto.nome().trim());
        usuario.setDataNascimento(dto.dataNascimento());
        usuario.setEmail(dto.email().trim());
        usuario.setTelefone(dto.telefone());
        usuario.setNomePai(dto.nomePai());
        usuario.setNomeMae(dto.nomeMae());
        usuario.setEstadoCivil(dto.estadoCivil());
        usuario.setEscolaridade(dto.escolaridade());
        usuario.setLocalNascimento(dto.localNascimento());
        usuario.setPaisResidencia(dto.paisResidencia());
        usuario.setZonaResidencial(dto.zonaResidencial());
        usuario.setNumeroResidencia(dto.numeroResidencia());
        if (usuario.getSexo() == null) {
            usuario.setSexo(dto.sexo());
        }
        if (usuario.getNacionalidade() == null) {
            usuario.setNacionalidade(dto.nacionalidade());
        }
    }

    /*
    Endereço: o CEP é conferido no ViaCEP. Cidade e UF vêm da consulta
    (não confiamos cegamente no que o front mandou). Se o ViaCEP estiver
    fora do ar, o cadastro continua com os dados informados.
     */
    private void preencherEndereco(UsuarioRequestDTO dto, Usuario usuario) {

        usuario.setLogradouro(dto.logradouro());
        usuario.setBairro(dto.bairro());
        usuario.setMunicipio(dto.municipio());
        usuario.setUf(dto.uf());
        usuario.setEstado(dto.uf());

        // O CEP é opcional no formulário
        if (dto.cep() == null || dto.cep().isBlank()) {
            usuario.setCep(null);
            return;
        }

        String cep = regrasCadastro.normalizarCep(dto.cep());
        usuario.setCep(cep);

        try {
            EnderecoResponseDTO endereco = cepService.buscar(cep);

            usuario.setMunicipio(endereco.municipio());
            usuario.setUf(endereco.uf());
            usuario.setEstado(endereco.uf());

            // CEPs de cidade pequena não têm rua nem bairro: mantém o que o cidadão digitou
            if (endereco.logradouro() != null && !endereco.logradouro().isBlank()) {
                usuario.setLogradouro(endereco.logradouro());
            }
            if (endereco.bairro() != null && !endereco.bairro().isBlank()) {
                usuario.setBairro(endereco.bairro());
            }
        } catch (RegraDeNegocioException e) {
            if (e.getStatus() == HttpStatus.NOT_FOUND) {
                throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "CEP não encontrado.");
            }
            // ViaCEP indisponível: segue com o endereço informado
        }
    }

    // O login é único para os 3 perfis, então o e-mail não pode se repetir entre eles
    private void validarEmailDisponivel(String email) {
        boolean emUso = usuarioRepository.findByEmail(email) != null
                || administradorRepository.findByEmailAdmin(email) != null
                || medicoRepository.findByEmailMedico(email) != null;

        if (emUso) {
            throw new RegraDeNegocioException(HttpStatus.CONFLICT, "Este e-mail já está cadastrado.");
        }
    }
}