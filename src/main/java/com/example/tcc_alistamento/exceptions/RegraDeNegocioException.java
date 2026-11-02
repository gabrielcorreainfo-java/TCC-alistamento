package com.example.tcc_alistamento.exceptions;
import org.springframework.http.HttpStatus;

// Classe para determinar os erros da nova regra do sistama como (CPF repetido, idade, sem permissão, não encontrado)

public class RegraDeNegocioException extends RuntimeException {
    private final HttpStatus status;

    public RegraDeNegocioException(HttpStatus status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
