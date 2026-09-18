package com.example.tcc_alistamento.exceptions;

import com.example.tcc_alistamento.controller.LocalController;
import com.example.tcc_alistamento.model.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/*
Captura as exceções e tranforma em resposta HTTP
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /*
     Trata a exceção UsuarioNotFoundException.
     Quando um usuário não é encontrado, este método é chamado
     automaticamente e transforma a exceção em uma resposta HTTP
     com status 404 (NOT_FOUND) e uma mensagem de erro.
     Devolvendo um objeto da classe ErrorResponse
     Response Entity - controla a resposta HHTP que API vai devolver
     */
    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ErrorResponse> tratarUsuarioNaoEncontrado(UsuarioNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AdministradorNotFoundException.class)
    public ResponseEntity<ErrorResponse> tratarAdministradorNaoEncontrado(AdministradorNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AlistamentoNotFoundException.class)
    public ResponseEntity<ErrorResponse> tratarAlistamentoNaoEncontrado(AlistamentoNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(LocalNotFoundException.class)
    public ResponseEntity<ErrorResponse> tratarLocalNaoEncontrado(LocalNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(TipoDeDocumentoNotFoundException.class)
    public ResponseEntity<ErrorResponse> tratarTipoDocumentoNaoEncontrado(TipoDeDocumentoNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DocumentoNotFoundException.class)
    public ResponseEntity<ErrorResponse> tratarDocumentoNaoEncontrado(DocumentoNotFoundException ex) {

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MedicoNotFoundException.class)
    public ResponseEntity<ErrorResponse> tratarMedicoNaoEncontrado(MedicoNotFoundException ex) {

        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AvaliacaoMedicaNotFoundException.class)
    public ResponseEntity<ErrorResponse> tratarAvaliacaoMedicaNaoEncontrada(
            AvaliacaoMedicaNotFoundException ex) {

        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(AgendamentoNotFoundException.class)
    public ResponseEntity<ErrorResponse> tratarAgendamentoNaoEncontrado(
            AgendamentoNotFoundException ex) {

        ErrorResponse errorResponse =
                new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }
}
