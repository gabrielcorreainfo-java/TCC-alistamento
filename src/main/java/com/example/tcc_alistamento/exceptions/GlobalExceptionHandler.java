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
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AvaliacaoMedicaNotFoundException.class)
    public ResponseEntity<ErrorResponse> tratarAvaliacaoMedicaNaoEncontrada(AvaliacaoMedicaNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AgendamentoNotFoundException.class)
    public ResponseEntity<ErrorResponse> tratarAgendamentoNaoEncontrado(AgendamentoNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Regras de negócio e de acesso (idade, CPF repetido, dado de outro cidadão...)
    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErrorResponse> tratarRegraDeNegocio(RegraDeNegocioException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getStatus(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    // Validações dos DTOs (@NotBlank, @Email...): devolve a mensagem do primeiro campo inválido
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> tratarValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos.");
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, mensagem);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Erros do banco: valor repetido em coluna UNIQUE ou registro com vínculos
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

    // Login com e-mail ou senha errados (mensagem genérica de propósito)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> tratarFalhaDeLogin(AuthenticationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // Arquivo de documento maior que o limite configurado
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> tratarArquivoGrande(MaxUploadSizeExceededException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "O arquivo deve ter no máximo 5 MB.");
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
