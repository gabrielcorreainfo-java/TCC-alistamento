package com.example.tcc_alistamento.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

/*
  Criptografia dos dados pessoais (CPF, RG e CEP) - LGPD.
- AES:  esconde o dado e permite mostrar de novo (reversível).
- HMAC: gera uma impressão digital do CPF, sempre igual (para evitar CPF repetido).
A chave vem da variável de ambiente CRIPTO_CHAVE.
 */
@Component
public class Criptografia {

    private static final String PREFIXO = "enc:";                        // Marca os valores criptografados
    private static final int TAMANHO_IV = 12;                            // Tamanho do embrulho aleatório (AES-GCM)
    private static final int TAMANHO_TAG = 128;                          // Tamanho do lacre de integridade
    private static final SecureRandom ALEATORIO = new SecureRandom();    // Gera números aleatórios seguros

    // static porque o conversor do JPA é criado pelo Hibernate, fora do Spring
    private static SecretKeySpec chaveAes;     // Chave do AES (esconder e mostrar)
    private static SecretKeySpec chaveHmac;    // Chave do HMAC (impressão digital)

    @Value("${api.cripto.chave}")
    private String chaveConfigurada;

    // Roda uma vez, quando a API sobe
    @PostConstruct
    void iniciar() throws Exception {

        // Sem chave, ou com chave curta, a API não sobe
        if (chaveConfigurada == null || chaveConfigurada.length() < 32) {
            throw new IllegalStateException(
                    "Defina a variável de ambiente CRIPTO_CHAVE com pelo menos 32 caracteres.");
        }

        // Cria duas chaves diferentes de 256 bits a partir da chave configurada
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        chaveAes  = new SecretKeySpec(sha256.digest(("aes:"  + chaveConfigurada).getBytes(StandardCharsets.UTF_8)), "AES");
        chaveHmac = new SecretKeySpec(sha256.digest(("hmac:" + chaveConfigurada).getBytes(StandardCharsets.UTF_8)), "HmacSHA256");
    }

    // Esconde o dado antes de gravar no banco
    public static String criptografar(String texto) {

        // Valor vazio: não tem o que esconder
        if (texto == null || texto.isBlank()) {
            return texto;
        }
        try {
            // Cria o embrulho
            byte[] iv = new byte[TAMANHO_IV];
            // Sorteia o embrulho (muda toda vez)
            ALEATORIO.nextBytes(iv);

            // Pega a máquina de embaralhar (AES)
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            // Liga no modo esconder
            cipher.init(Cipher.ENCRYPT_MODE, chaveAes, new GCMParameterSpec(TAMANHO_TAG, iv));
            // Embaralha o dado
            byte[] cifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));

            // Caixa para o embrulho + dado
            byte[] resultado = new byte[iv.length + cifrado.length];
            // Coloca o embrulho no começo
            System.arraycopy(iv, 0, resultado, 0, iv.length);
            // Coloca o dado embaralhado depois
            System.arraycopy(cifrado, 0, resultado, iv.length, cifrado.length);
            // Vira texto e recebe o "enc:"
            return PREFIXO + Base64.getEncoder().encodeToString(resultado);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao criptografar dado pessoal.", e);
        }
    }

    // Mostra o dado de novo quando é lido do banco
    public static String descriptografar(String valor) {

        // Sem o "enc:", o valor não está criptografado: devolve como está
        if (valor == null || !valor.startsWith(PREFIXO)) {
            return valor;
        }
        try {
            // Tira o "enc:" e volta para bytes
            byte[] dados = Base64.getDecoder().decode(valor.substring(PREFIXO.length()));
            // Pega a máquina de embaralhar (AES)
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            // Liga no modo mostrar, usando o embrulho
            cipher.init(Cipher.DECRYPT_MODE, chaveAes,
                    new GCMParameterSpec(TAMANHO_TAG, dados, 0, TAMANHO_IV));
            // Desembaralha o dado
            byte[] texto = cipher.doFinal(dados, TAMANHO_IV, dados.length - TAMANHO_IV);
            // Volta para texto normal
            return new String(texto, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao descriptografar dado pessoal.", e);
        }
    }

    // Impressão digital do CPF (sempre igual para o mesmo CPF), usada para evitar CPF repetido
    public static String hash(String texto) {

        // Valor vazio: não tem digital
        if (texto == null) {
            return null;
        }
        try {
            // Pega a máquina de digitais (HMAC)
            Mac mac = Mac.getInstance("HmacSHA256");
            // Liga com a chave do HMAC
            mac.init(chaveHmac);
            // Gera a digital (64 caracteres)
            return HexFormat.of().formatHex(mac.doFinal(texto.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao gerar hash do dado pessoal.", e);
        }
    }
}