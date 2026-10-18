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

@Component
public class Criptografia {
    private static final String PREFIXO = "enc:";   // marca os valores já criptografados
    private static final int TAMANHO_IV = 12;        // tamanho recomendado para o GCM
    private static final int TAMANHO_TAG = 128;      // bits do código de integridade

    private static final SecureRandom ALEATORIO = new SecureRandom();

    // static porque o conversor do JPA é criado pelo Hibernate, fora do Spring
    private static SecretKeySpec chaveAes;
    private static SecretKeySpec chaveHmac;

    @Value("${api.cripto.chave}")
    private String chaveConfigurada;

    @PostConstruct
    void iniciar() throws Exception {
        if (chaveConfigurada == null || chaveConfigurada.length() < 32) {
            throw new IllegalStateException(
                    "Defina a variável de ambiente CRIPTO_CHAVE com pelo menos 32 caracteres.");
        }
        // Deriva duas chaves de 256 bits diferentes a partir da chave configurada
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        chaveAes = new SecretKeySpec(
                sha256.digest(("aes:" + chaveConfigurada).getBytes(StandardCharsets.UTF_8)), "AES");
        chaveHmac = new SecretKeySpec(
                sha256.digest(("hmac:" + chaveConfigurada).getBytes(StandardCharsets.UTF_8)), "HmacSHA256");
    }

    public static String criptografar(String texto) {
        if (texto == null || texto.isBlank()) {
            return texto;
        }
        try {
            // IV aleatório: o mesmo CPF gera um texto criptografado diferente a cada vez
            byte[] iv = new byte[TAMANHO_IV];
            ALEATORIO.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, chaveAes, new GCMParameterSpec(TAMANHO_TAG, iv));
            byte[] cifrado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));

            // Guarda IV + texto cifrado juntos, em Base64
            byte[] resultado = new byte[iv.length + cifrado.length];
            System.arraycopy(iv, 0, resultado, 0, iv.length);
            System.arraycopy(cifrado, 0, resultado, iv.length, cifrado.length);

            return PREFIXO + Base64.getEncoder().encodeToString(resultado);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao criptografar dado pessoal.", e);
        }
    }

    public static String descriptografar(String valor) {
        // Valores antigos, gravados antes da criptografia, são devolvidos como estão
        if (valor == null || !valor.startsWith(PREFIXO)) {
            return valor;
        }
        try {
            byte[] dados = Base64.getDecoder().decode(valor.substring(PREFIXO.length()));

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, chaveAes, new GCMParameterSpec(TAMANHO_TAG, dados, 0, TAMANHO_IV));
            byte[] texto = cipher.doFinal(dados, TAMANHO_IV, dados.length - TAMANHO_IV);

            return new String(texto, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao descriptografar dado pessoal.", e);
        }
    }

    // Impressão digital do CPF (sempre igual para o mesmo CPF), usada para evitar duplicidade
    public static String hash(String texto) {
        if (texto == null) {
            return null;
        }
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(chaveHmac);
            return HexFormat.of().formatHex(mac.doFinal(texto.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao gerar hash do dado pessoal.", e);
        }
    }
}
