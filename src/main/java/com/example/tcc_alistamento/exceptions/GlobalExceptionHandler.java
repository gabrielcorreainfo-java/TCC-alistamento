package com.example.tcc_alistamento.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.security.core.AuthenticationException;

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

    // Regra de negócio quebrada (CPF repetido, idade, sem permissão, não encontrado): usa o código e a mensagem do erro
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErrorResponse> tratarRegraDeNegocio(RegraDeNegocioException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    // Campo inválido no @Valid (vazio, e-mail errado): 400 com a mensagem do primeiro campo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> tratarValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos.");
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, mensagem);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Banco recusou (valor repetido ou registro com vínculo): 409 sem mostrar o erro do MySQL
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> tratarIntegridadeDoBanco(DataIntegrityViolationException ex) {
        String causa = ex.getMostSpecificCause().getMessage();
        String mensagem;
        if (causa != null && causa.contains("Duplicate entry")) {
            mensagem = "Já existe um cadastro com esses dados.";
        } else if (causa != null && causa.contains("foreign key")) {
            mensagem = "Não é possível concluir: este registro está vinculado a outros dados.";
        } else {
            mensagem = "Os dados informados não são válidos.";
        }
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, mensagem);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // E-mail ou senha errados: 401 com mensagem genérica (não revela se o e-mail existe)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> tratarFalhaDeLogin(AuthenticationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // Arquivo maior que 5 MB: 400
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> tratarArquivoGrande(MaxUploadSizeExceededException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "O arquivo deve ter no máximo 5 MB.");
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
