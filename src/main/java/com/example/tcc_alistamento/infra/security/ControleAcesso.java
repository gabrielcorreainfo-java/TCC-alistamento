package com.example.tcc_alistamento.infra.security;

import com.example.tcc_alistamento.enums.RolesUsers;
import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import com.example.tcc_alistamento.model.Alistamento;
import com.example.tcc_alistamento.security.UsuarioAutenticado;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
public class ControleAcesso {
    // Quem fez a requisição (preenchido pelo SecurityFilter a partir do token)
    public UsuarioAutenticado atual() {
        Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacao != null && autenticacao.getPrincipal() instanceof UsuarioAutenticado usuario) {
            return usuario;
        }
        throw new RegraDeNegocioException(HttpStatus.UNAUTHORIZED, "Faça login para continuar.");
    }

    public boolean ehCidadao() {
        return RolesUsers.USER.getRole().equals(atual().getPerfil());
    }

    public boolean ehMedico() {
        return RolesUsers.MEDICO.getRole().equals(atual().getPerfil());
    }

    public boolean ehAdmin() {
        return RolesUsers.ADMIN.getRole().equals(atual().getPerfil());
    }

    // O cidadão só pode acessar o próprio cadastro
    public void exigirDonoDoUsuario(Integer idUsuario) {
        if (ehCidadao() && !atual().getId().equals(idUsuario)) {
            throw semPermissao();
        }
    }

    // O cidadão só pode acessar o próprio alistamento
    public void exigirDonoDoAlistamento(Alistamento alistamento) {
        if (ehCidadao() && !atual().getId().equals(alistamento.getUsuario().getId())) {
            throw semPermissao();
        }
    }

    public RegraDeNegocioException semPermissao() {
        return new RegraDeNegocioException(HttpStatus.FORBIDDEN,
                "Você não tem permissão para acessar este registro.");
    }
}
