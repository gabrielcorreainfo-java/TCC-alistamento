package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.util.Set;

@Component
public class RegrasCadastro {
    public static final String MASCULINO = "Masculino";
    public static final String FEMININO = "Feminino";

    private static final Set<String> SEXOS = Set.of(MASCULINO, FEMININO);
    private static final Set<String> NACIONALIDADES = Set.of("Brasileira", "Brasileira naturalizada");

    /*
    Lei nº 4.375/1964 (Lei do Serviço Militar): a obrigação começa no ano em que
    o cidadão completa 18 anos e vai até o ano em que completa 45.
    Alistamento feminino: voluntário, no ano em que a candidata completa 18 anos.
    (Mesma regra usada pelo front, na função erroIdadeAlistamento.)
     */
    private static final int IDADE_ALISTAMENTO = 18;
    private static final int IDADE_MAXIMA_MASCULINO = 45;

    // ----- Sexo, nacionalidade e idade -----

    public void validarSexo(String sexo) {
        if (sexo == null || !SEXOS.contains(sexo)) {
            throw erro("Informe o sexo: Masculino ou Feminino.");
        }
    }

    public void validarNacionalidade(String nacionalidade) {
        if (nacionalidade == null || !NACIONALIDADES.contains(nacionalidade)) {
            throw erro("Somente brasileiros natos ou naturalizados podem realizar o alistamento militar.");
        }
    }

    // Confere se a pessoa pode abrir o alistamento (usado no cadastro e no alistamento)
    public void validarElegibilidade(LocalDate dataNascimento, String sexo, String nacionalidade) {

        validarSexo(sexo);
        validarNacionalidade(nacionalidade);

        // Data de nascimento nula ou futura é inválida
        if (dataNascimento == null || dataNascimento.isAfter(LocalDate.now())) {
            throw erro("Data de nascimento inválida.");
        }

        // Calcula a idade pelo ano atual
        int idadeNoAno = Year.now().getValue() - dataNascimento.getYear();

        if (idadeNoAno < IDADE_ALISTAMENTO) {
            throw erro("O alistamento só é permitido a partir do ano em que você completa 18 anos.");
        }

        if (FEMININO.equals(sexo) && idadeNoAno > IDADE_ALISTAMENTO) {
            throw erro("O alistamento feminino é voluntário e só pode ser feito no ano em que a candidata completa 18 anos.");
        }

        if (MASCULINO.equals(sexo) && idadeNoAno > IDADE_MAXIMA_MASCULINO) {
            throw erro("Não há obrigação de alistamento após o ano em que se completa 45 anos.");
        }
    }

    // ----- LGPD -----

    public void validarConsentimento(Boolean consentimento) {
        if (!Boolean.TRUE.equals(consentimento)) {
            throw erro("É necessário aceitar a Política de Privacidade para concluir o cadastro.");
        }
    }

    // ----- Documentos e endereço -----

    // Devolve o CPF só com os 11 números, conferindo os dígitos verificadores
    public String normalizarCpf(String cpf) {
        String numeros = cpf == null ? "" : cpf.replaceAll("\\D", "");

        // Se não tem 11 numeros, ou se todos os números são iguais erro
        if (numeros.length() != 11 || numeros.chars().distinct().count() == 1) {
            throw erro("CPF inválido.");
        }

        // Confere os dois ultimos números do CPF calculado apartir dos 9 primeiros
        for (int posicao = 9; posicao <= 10; posicao++) {
            int soma = 0;
            for (int i = 0; i < posicao; i++) {
                soma += (numeros.charAt(i) - '0') * (posicao + 1 - i);
            }
            int digito = (soma * 10) % 11;
            if (digito == 10) {
                digito = 0;
            }
            if (digito != numeros.charAt(posicao) - '0') {
                throw erro("CPF inválido.");
            }
        }
        return numeros;
    }

    /*
    O formato do RG muda de estado para estado, então não existe uma regra única.
    Aceitamos números com pontos, traço ou barra, de 6 a 14 dígitos,
    e o último dígito pode ser X (mesma regra do front, na função rgValido).
     */
    public String validarRg(String rg) {
        // Se RG veio nulo usa "" senão trim() tira os espaçps
        String texto = rg == null ? "" : rg.trim();

        // Confere os caracteres se só tem permitidos
        if (!texto.matches("[0-9xX.\\-/ ]+")) {
            throw erro("RG inválido: use apenas números, pontos, traço e o dígito X.");
        }

        // Oque não é numero nem X, apaga isso e deixa o X maiúsculo
        String normalizado = texto.replaceAll("[^0-9xX]", "").toUpperCase();

        // X só pode estra no final
        if (!normalizado.matches("[0-9]{5,13}[0-9X]")) {
            throw erro("RG inválido.");
        }
        return texto;
    }

    // Devolve o CEP no formato 00000-000
    public String normalizarCep(String cep) {
        // Se veio nulo deixa "" senão apaga tudo que não é número
        String numeros = cep == null ? "" : cep.replaceAll("\\D", "");
        if (numeros.length() != 8) {
            throw erro("CEP inválido. Use o formato 00000-000.");
        }
        // Pega do 6º até o fim e devolve formatado 06010-000
        return numeros.substring(0, 5) + "-" + numeros.substring(5);
    }

    public void validarUf(String uf) {
        // Só confere se veio preenchido, se não for 2 letras maiúsculas UF inválida
        if (uf != null && !uf.isBlank() && !uf.matches("[A-Z]{2}")) {
            throw erro("UF inválida.");
        }
    }

    // Metódo de atalho para todas as validações
    private RegraDeNegocioException erro(String mensagem) {
        return new RegraDeNegocioException(HttpStatus.BAD_REQUEST, mensagem);
    }
}
