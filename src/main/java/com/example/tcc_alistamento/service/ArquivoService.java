package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

/*
Guarda os arquivos dos documentos (PDF ou imagem) em uma pasta do servidor.

Cuidados de segurança:
- só aceita PDF, PNG e JPEG, conferindo o conteúdo real do arquivo
  (os primeiros bytes), e não só o nome ou o tipo informado pelo navegador;
- o arquivo é salvo com um nome aleatório (UUID), então ninguém consegue
  adivinhar o nome nem usar "../" para gravar fora da pasta;
- o tamanho máximo (5 MB) fica no application.properties.
 */
@Service
public class ArquivoService {

    private static final Map<String, String> EXTENSOES = Map.of(
            "application/pdf", ".pdf",
            "image/png", ".png",
            "image/jpeg", ".jpg"
    );

    private final Path pasta;

    public ArquivoService(@Value("${api.arquivos.pasta}") String pasta) throws IOException {
        this.pasta = Paths.get(pasta).toAbsolutePath().normalize();
        Files.createDirectories(this.pasta);
    }

    // Salva o arquivo e devolve o nome gerado
    public String salvar(MultipartFile arquivo) {

        if (arquivo == null || arquivo.isEmpty()) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "Selecione um arquivo.");
        }

        String tipo = detectarTipo(arquivo);
        String nome = UUID.randomUUID() + EXTENSOES.get(tipo);

        try (InputStream entrada = arquivo.getInputStream()) {
            Files.copy(entrada, pasta.resolve(nome), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RegraDeNegocioException(HttpStatus.INTERNAL_SERVER_ERROR, "Não foi possível salvar o arquivo.");
        }
        return nome;
    }

    public Resource carregar(String nome) {
        try {
            Path caminho = pasta.resolve(nome).normalize();
            Resource recurso = new UrlResource(caminho.toUri());
            if (!caminho.startsWith(pasta) || !recurso.exists()) {
                throw new RegraDeNegocioException(HttpStatus.NOT_FOUND, "Arquivo não encontrado.");
            }
            return recurso;
        } catch (MalformedURLException e) {
            throw new RegraDeNegocioException(HttpStatus.NOT_FOUND, "Arquivo não encontrado.");
        }
    }

    public void apagar(String nome) {
        if (nome == null) {
            return;
        }
        try {
            Files.deleteIfExists(pasta.resolve(nome).normalize());
        } catch (IOException ignorado) {
            // Se não conseguir apagar o arquivo, o registro do banco é removido mesmo assim
        }
    }

    // Descobre o tipo pelos primeiros bytes do arquivo ("assinatura" do formato)
    public String detectarTipo(MultipartFile arquivo) {
        byte[] inicio = new byte[8];
        try (InputStream entrada = arquivo.getInputStream()) {
            int lidos = entrada.read(inicio);
            if (lidos >= 4 && inicio[0] == '%' && inicio[1] == 'P' && inicio[2] == 'D' && inicio[3] == 'F') {
                return "application/pdf";
            }
            if (lidos >= 4 && (inicio[0] & 0xFF) == 0x89 && inicio[1] == 'P' && inicio[2] == 'N' && inicio[3] == 'G') {
                return "image/png";
            }
            if (lidos >= 3 && (inicio[0] & 0xFF) == 0xFF && (inicio[1] & 0xFF) == 0xD8 && (inicio[2] & 0xFF) == 0xFF) {
                return "image/jpeg";
            }
        } catch (IOException e) {
            // cai na mensagem de erro abaixo
        }
        throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "Envie o documento em PDF, PNG ou JPG.");
    }
}