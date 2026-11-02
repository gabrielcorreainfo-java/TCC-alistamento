package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.request.RedefinirSenhaDTO;
import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import com.example.tcc_alistamento.model.Usuario;
import com.example.tcc_alistamento.repository.UsuarioRepository;
import com.example.tcc_alistamento.security.criptografia.Criptografia;
import com.example.tcc_alistamento.service.integracao.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;


/*
Recuperação de senha por código enviado por e-mail:
1. O cidadão informa o e-mail e recebe um código de 6 dígitos.
2. Ele informa o código e a senha nova.
Só o hash do código fica no banco; ele vence em 10 minutos, é de uso único
e aceita no máximo 5 erros. A resposta do pedido é sempre a mesma.
 */

@Service
public class RecuperacaoSenhaService {

    // O código vale por 10 minutos
    private static final int MINUTOS_VALIDADE = 10;
    // Máximo de erros antes de cancelar o código
    private static final int MAX_TENTATIVAS = 5;
    // Gerador de números aleatórios seguro
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public RecuperacaoSenhaService(UsuarioRepository usuarioRepository,
                                   EmailService emailService,
                                   PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // O cidadão pede uma senha nova
    public void solicitar(String email) {

        // Email vazio
        if (email == null || email.isBlank()) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "Informe o e-mail.");
        }

        // Procura o cidadão e tira os espaços
        Usuario usuario = usuarioRepository.findByEmail(email.trim());

        // E-mail não cadastrado: não faz nada, mas a resposta é a mesma
        if (usuario == null) {
            return;
        }

        // Sorteia um código de 6 dígitos (ex.: 048213)
        String codigo = String.format("%06d", ALEATORIO.nextInt(1_000_000));

        // Guarda só o hash, a validade e zera as tentativas (substitui um código antigo)
        usuario.setCodigoSenhaHash(Criptografia.hash(codigo));
        usuario.setCodigoSenhaExpira(LocalDateTime.now().plusMinutes(MINUTOS_VALIDADE));
        usuario.setCodigoSenhaTentativas(0);
        usuarioRepository.save(usuario);

        // Envia o código por e-mail
        emailService.enviarCodigoSenha(usuario.getEmail(), codigo, MINUTOS_VALIDADE);

        }

    // O cidadão informa o código e a senha nova
    public void redefinir(RedefinirSenhaDTO dto) {

        // Campos vazios: 400
        if (dto.email() == null || dto.codigo() == null || dto.novaSenha() == null) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST,
                    "Preencha o e-mail, o código e a nova senha.");
        }

        // Procura o cidadão
        Usuario usuario = usuarioRepository.findByEmail(dto.email().trim());

        // Sem cidadão, sem código ou código vencido: 400
        if (usuario == null
                || usuario.getCodigoSenhaHash() == null
                || usuario.getCodigoSenhaExpira().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "Código inválido ou expirado.");
        }

        // Errou demais: cancela o código
        if (usuario.getCodigoSenhaTentativas() >= MAX_TENTATIVAS) {
            limparCodigo(usuario);
            usuarioRepository.save(usuario);
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST,
                    "Muitas tentativas. Peça um novo código.");
        }

        // Código errado: soma uma tentativa
        if (!Criptografia.hash(dto.codigo().trim()).equals(usuario.getCodigoSenhaHash())) {
            usuario.setCodigoSenhaTentativas(usuario.getCodigoSenhaTentativas() + 1);
            usuarioRepository.save(usuario);
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "Código inválido.");
        }

        // Senha nova curta: 400
        if (dto.novaSenha().length() < 6) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST,
                    "A senha deve ter pelo menos 6 caracteres.");
        }

        // Tudo certo: grava a senha nova com BCrypt e apaga o código (uso único)
        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        limparCodigo(usuario);
        usuarioRepository.save(usuario);
    }

    // Apaga o código do cadastro
    private void limparCodigo(Usuario usuario) {
        usuario.setCodigoSenhaHash(null);
        usuario.setCodigoSenhaExpira(null);
        usuario.setCodigoSenhaTentativas(0);
    }
}
