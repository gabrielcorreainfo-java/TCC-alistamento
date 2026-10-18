package com.example.tcc_alistamento.exceptions;

import org.springframework.http.HttpStatus;

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
