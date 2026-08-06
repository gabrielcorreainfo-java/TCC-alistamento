package com.example.tcc_alistamento.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
// Uma Classe que representa a resposta HTTP que retornara em um JSON
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private HttpStatus status;
    private String mensagem;
}
