package com.example.tcc_alistamento.dto;

import com.example.tcc_alistamento.model.Usuario;

import java.time.LocalDate;

public record UsuarioResponseDTO(Integer id,
                                 String nome,
                                 LocalDate dataNascimento,
                                 String email,
                                 String telefone,
                                 String cpf,
                                 String nomePai,
                                 String nomeMae,
                                 String estadoCivil,
                                 String uf,
                                 String escolaridade,
                                 String rg,
                                 String localNascimento,
                                 String cep,
                                 String bairro,
                                 String municipio,
                                 String paisResidencia,
                                 String zonaResidencial,
                                 String numeroResidencia,
                                 String logradouro,
                                 String estado) {

    public UsuarioResponseDTO(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getDataNascimento(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getCpf(),
                usuario.getNomePai(),
                usuario.getNomeMae(),
                usuario.getEstadoCivil(),
                usuario.getUf(),
                usuario.getEscolaridade(),
                usuario.getRg(),
                usuario.getLocalNascimento(),
                usuario.getCep(),
                usuario.getBairro(),
                usuario.getMunicipio(),
                usuario.getPaisResidencia(),
                usuario.getZonaResidencial(),
                usuario.getNumeroResidencia(),
                usuario.getLogradouro(),
                usuario.getEstado()
        );
    }
}
