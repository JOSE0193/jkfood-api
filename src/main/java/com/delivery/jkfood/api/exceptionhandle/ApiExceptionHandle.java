package com.delivery.jkfood.api.exceptionhandle;

import com.delivery.jkfood.domain.exception.EntidadeEmUsoException;
import com.delivery.jkfood.domain.exception.EntidadeNaoEncontradaException;
import com.delivery.jkfood.domain.exception.NegocioException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandle extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request){
        ExceptionDetails details = createExceptionBuilder(HttpStatus.NOT_FOUND, ExceptionType.ENTIDADE_NAO_ENCONTRADA,
                ex.getMessage()).build();
        return handleExceptionInternal(ex, details, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {
        ExceptionDetails details = createExceptionBuilder(HttpStatus.BAD_REQUEST, ExceptionType.ERRO_NEGOCIO,
                ex.getMessage()).build();
        return handleExceptionInternal(ex, details, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {
        ExceptionDetails details = createExceptionBuilder(HttpStatus.CONFLICT, ExceptionType.ENTIDADE_EM_USO,
                ex.getMessage()).build();
        return handleExceptionInternal(ex, details, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String detail = "O corpo da requisição está invalido, verifique na sintaxe";
        ExceptionDetails exceptionDetails = createExceptionBuilder(HttpStatus.BAD_REQUEST, ExceptionType.MENSAGEM_INCOMPREENSIVEL,
                detail).build();
        return super.handleExceptionInternal(ex, exceptionDetails, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        if(body == null){
            body = ExceptionDetails.builder()
                    .status(statusCode.value())
                    .timestamp(LocalDateTime.now())
                    .title(statusCode.toString())
                    .build();
        } else if (body instanceof String) {
            body = ExceptionDetails.builder()
                    .status(statusCode.value())
                    .timestamp(LocalDateTime.now())
                    .title((String) body)
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    private ExceptionDetails.ExceptionDetailsBuilder createExceptionBuilder(HttpStatus status, ExceptionType type,
                                                                            String detail){
        return ExceptionDetails.builder()
                .status(status.value())
                .type(type.getUri())
                .title(type.getTitle())
                .detail(detail)
                .timestamp(LocalDateTime.now());
    }

}
