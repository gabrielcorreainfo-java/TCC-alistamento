package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.PedidoSenhaResponseDTO;
import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import com.example.tcc_alistamento.model.PedidoSenha;
import com.example.tcc_alistamento.repository.PedidoSenhaRepository;
import com.example.tcc_alistamento.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/*
Recuperação de senha (versão com atendimento pelo administrador):

1. O cidadão informa o e-mail na tela "Esqueci minha senha".
2. A API registra um pedido, SEM dizer se o e-mail existe ou não.
   (Se dissesse, alguém poderia descobrir quem tem cadastro - LGPD.)
3. O administrador vê os pedidos, redefine a senha (gravada com BCrypt)
   e entra em contato com o cidadão.
 */
@Service
public class RecuperacaoSenhaService {

    private final PedidoSenhaRepository pedidoSenhaRepository;
    private final UsuarioRepository usuarioRepository;

    public RecuperacaoSenhaService(PedidoSenhaRepository pedidoSenhaRepository,
                                   UsuarioRepository usuarioRepository) {
        this.pedidoSenhaRepository = pedidoSenhaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void solicitar(String email) {

        if (email == null || email.isBlank()) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "Informe o e-mail.");
        }

        String emailNormalizado = email.trim();

        // Só registra se o e-mail for de um cidadão e não houver pedido pendente.
        // Em qualquer caso, a resposta para o front é a mesma.
        boolean existe = usuarioRepository.findByEmail(emailNormalizado) != null;
        boolean jaPendente = pedidoSenhaRepository
                .existsByEmailIgnoreCaseAndStatus(emailNormalizado, PedidoSenha.PENDENTE);

        if (existe && !jaPendente) {
            PedidoSenha pedido = new PedidoSenha();
            pedido.setEmail(emailNormalizado);
            pedido.setDataPedido(LocalDateTime.now());
            pedidoSenhaRepository.save(pedido);
        }
    }

    public List<PedidoSenhaResponseDTO> listarPendentes() {
        return pedidoSenhaRepository.findByStatusOrderByDataPedidoAsc(PedidoSenha.PENDENTE)
                .stream()
                .map(PedidoSenhaResponseDTO::new)
                .toList();
    }

    public void concluir(Integer id) {
        PedidoSenha pedido = pedidoSenhaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException(HttpStatus.NOT_FOUND, "Pedido não encontrado."));

        pedido.setStatus(PedidoSenha.ATENDIDO);
        pedido.setDataAtendimento(LocalDateTime.now());
        pedidoSenhaRepository.save(pedido);
    }
}