package com.example.tcc_alistamento.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UsuarioRequestDTO(@NotBlank(message = "Informe o nome.")
                                @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
                                String nome,

                                @NotNull(message = "Informe a data de nascimento.")
                                @Past(message = "A data de nascimento deve estar no passado.")
                                LocalDate dataNascimento,

                                @NotBlank(message = "Informe o e-mail.")
                                @Email(message = "E-mail inválido.")
                                String email,

                                // Obrigatória só no cadastro (conferido no UsuarioService)
                                String senha,

                                String telefone,

                                @NotBlank(message = "Informe o CPF.")
                                String cpf,

                                String nomePai,

                                @NotBlank(message = "Informe o nome da mãe.")
                                String nomeMae,

                                String estadoCivil,

                                String uf,

                                String escolaridade,

                                @NotBlank(message = "Informe o RG.")
                                String rg,

                                String localNascimento,

                                // Opcional no formulário; quando informado, é validado e consultado no ViaCEP
                                String cep,

                                String bairro,

                                String municipio,

                                String paisResidencia,

                                String zonaResidencial,

                                String numeroResidencia,

                                String logradouro,

                                String estado,

                                // Masculino ou Feminino
                                String sexo,

                                // Brasileira ou Brasileira naturalizada
                                String nacionalidade,

                                // LGPD: o cidadão aceitou a Política de Privacidade
                                Boolean consentimentoLgpd) {
}
