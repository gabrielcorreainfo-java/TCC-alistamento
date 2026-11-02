package com.example.tcc_alistamento.service;

import com.example.tcc_alistamento.dto.EnderecoResponseDTO;
import com.example.tcc_alistamento.exceptions.RegraDeNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;

@Service
public class CepService {
    // Formato da resposta do ViaCEP manda CEP e devolve o endereço
    private record RespostaViaCep(String cep, String logradouro, String bairro,
                                  String localidade, String uf, Object erro) {
    }

    // ferramenta para chamar outro site (ViaCep)
    private final RestClient restClient;
    // para conferir o formato do CEP
    private final RegrasCadastro regrasCadastro;

    public CepService(RegrasCadastro regrasCadastro) {
        this.regrasCadastro = regrasCadastro;

        // Limite de 5 segundos para não travar a API se o ViaCEP demorar
        SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(Duration.ofSeconds(5));
        fabrica.setReadTimeout(Duration.ofSeconds(5));

        // Prepara o RestClient com o endereço do ViaCEP
        this.restClient = RestClient.builder()
                .baseUrl("https://viacep.com.br/ws")
                .requestFactory(fabrica)
                .build();
    }

    /*
    Devolve o endereço do CEP.
    - CEP com formato errado: 400
    - CEP que não existe:      404
    - ViaCEP fora do ar:       503
     */
    public EnderecoResponseDTO buscar(String cep) {
        // Confere o formato e devolve "06010-000" (se errado: 400)
        String cepFormatado = regrasCadastro.normalizarCep(cep);
        // Tira o traço para mandar ao ViaCEP: "06010000"
        String numeros = cepFormatado.replace("-", "");

        RespostaViaCep resposta;
        try {
            // Chama https://viacep.com.br/ws/06010000/json/
            resposta = restClient.get()
                    .uri("/{cep}/json/", numeros)
                    .retrieve()
                    .body(RespostaViaCep.class); // transforma o JSON no record
        } catch (RestClientException e) {
            // ViaCEP fora do ar ou demorou mais de 5 segundos: 503
            throw new RegraDeNegocioException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Serviço de consulta de CEP indisponível no momento.");
        }

        // O ViaCEP responde {"erro": true} quando o CEP não existe
        if (resposta == null || resposta.erro() != null) {
            throw new RegraDeNegocioException(HttpStatus.NOT_FOUND, "CEP não encontrado.");
        }

        return new EnderecoResponseDTO(
                cepFormatado,
                resposta.logradouro(),
                resposta.bairro(),
                resposta.localidade(),
                resposta.uf()
        );
    }
}
