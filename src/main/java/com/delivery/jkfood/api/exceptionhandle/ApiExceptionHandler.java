package com.delivery.jkfood.api.exceptionhandle;

import com.delivery.jkfood.domain.exception.EntidadeEmUsoException;
import com.delivery.jkfood.domain.exception.EntidadeNaoEncontradaException;
import com.delivery.jkfood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se o" +
            " problema persistir contate o administrador do sistema.";

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionType type = ExceptionType.RECURSO_NAO_ENCONTRADO;
        String detail = ex.getMessage();
        ExceptionDetails details = createExceptionBuilder(status, type, detail)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();
        return handleExceptionInternal(ex, details, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionType type = ExceptionType.ERRO_NEGOCIO;
        String detail = ex.getMessage();
        ExceptionDetails details = createExceptionBuilder(status, type, detail)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();
        return handleExceptionInternal(ex, details, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        ExceptionType type = ExceptionType.ENTIDADE_EM_USO;
        String detail = ex.getMessage();
        ExceptionDetails exceptionDetails = createExceptionBuilder(status, type, detail)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();
        return handleExceptionInternal(ex, exceptionDetails, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUncaught(Exception ex, WebRequest webRequest) throws Exception{
        ExceptionType type = ExceptionType.ERRO_DE_SISTEMA;
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String details = String.format(MSG_ERRO_GENERICA_USUARIO_FINAL);
        ex.printStackTrace();
        ExceptionDetails exceptionDetails = createExceptionBuilder(httpStatus, type, details)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();
        return handleExceptionInternal(ex, exceptionDetails, new HttpHeaders(), httpStatus, webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatusCode status, WebRequest request) {
        if(ex instanceof MethodArgumentTypeMismatchException){
            return handleMethodArgumentTypeMismatchException((MethodArgumentTypeMismatchException)ex, headers, status, request);
        }
        return super.handleTypeMismatch(ex, headers, status, request);
    }

    private ResponseEntity<Object> handleMethodArgumentTypeMismatchException (MethodArgumentTypeMismatchException ex, HttpHeaders headers,
                                                                              HttpStatusCode statusCode, WebRequest request) {
        ExceptionType exceptionType = ExceptionType.PARAMETRO_INVALIDO;
        String details = String.format("O parâmetro de URL '%s' recebeu o valor '%s', que é de um tipo inválido. Corrija e informe um " +
                "valor compatível com o tipo '%s'.", ex.getParameter().getParameterName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        ExceptionDetails exceptionDetails = createExceptionBuilder(statusCode, exceptionType, details)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();
        return handleExceptionInternal(ex, exceptionDetails, headers, statusCode, request);

    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatusCode status, WebRequest request) {
        ExceptionType type = ExceptionType.RECURSO_NAO_ENCONTRADO;
        String details = String.format("O Recurso '%s', que você tentou acessar, é inexistente", ex.getRequestURL());
        ExceptionDetails exceptionDetails = createExceptionBuilder(status, type, details)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();
        return this.handleExceptionInternal(ex, exceptionDetails, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
                                                                    HttpStatusCode status, WebRequest request) {
        ExceptionType type = ExceptionType.RECURSO_NAO_ENCONTRADO;
        String details = String.format("O Recurso '%s', que você tentou acessar, é inexistente", ex.getResourcePath());
        ExceptionDetails exceptionDetails = createExceptionBuilder(status, type, details)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();
        return this.handleExceptionInternal(ex, exceptionDetails, headers, status, request);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if(rootCause instanceof InvalidFormatException){
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if(rootCause instanceof PropertyBindingException){
            return handlePropertyBidingException((PropertyBindingException) rootCause, headers, status, request);
        }
        ExceptionType exceptionType = ExceptionType.MENSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisição está invalido, verifique na sintaxe";
        ExceptionDetails exceptionDetails = createExceptionBuilder(status, exceptionType, detail)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();
        return super.handleExceptionInternal(ex, exceptionDetails, new HttpHeaders(), status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
                                                                HttpStatusCode status, WebRequest request) {
        String path = joinPath(ex.getPath());
        ExceptionType exceptionType = ExceptionType.MENSAGEM_INCOMPREENSIVEL;
        String details = String.format("A propriedade '%s' recebeu o valor '%s', " +
                        "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.", path,
                ex.getValue(), ex.getTargetType().getSimpleName());
        ExceptionDetails exceptionDetails = createExceptionBuilder(status, exceptionType, details)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();

        return handleExceptionInternal(ex, exceptionDetails, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBidingException(PropertyBindingException ex, HttpHeaders headers,
                                                                 HttpStatusCode statusCode, WebRequest request){
        ExceptionType exceptionType = ExceptionType.MENSAGEM_INCOMPREENSIVEL;
        String path = joinPath(ex.getPath());
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        String details = "";
        if(rootCause instanceof UnrecognizedPropertyException) {
            details = String.format("A propriedade '%s' não é um parâmetro compatível com a requisição esperada pela api, " +
                            "remova a propriedade e tente novamente", ex.getPropertyName());
        } else if (rootCause instanceof IgnoredPropertyException){
            details = String.format("A propriedade '%s' não existe, remova ou corrija a propriedade e tente novamente",
                    ex.getPropertyName());
        }
        ExceptionDetails exceptionDetails = createExceptionBuilder(statusCode, exceptionType, details)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();
        return handleExceptionInternal(ex, exceptionDetails, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        ExceptionType type = ExceptionType.DADOS_INVALIDOS;
        String details = String.format("Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.");
        BindingResult bindingResult = ex.getBindingResult();
        List<ExceptionDetails.Field> exceptionFields = bindingResult.getFieldErrors().stream()
                .map(fieldError -> {
                    String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
                    return ExceptionDetails.Field.builder()
                            .name(fieldError.getField())
                            .userMessage(message)
                            .build();
                })
                .collect(Collectors.toList());

        ExceptionDetails exceptionDetails = createExceptionBuilder(status, type, details)
                .userMessage(details)
                .fields(exceptionFields)
                .build();
        return handleExceptionInternal(ex, exceptionDetails, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        if(body == null){
            body = ExceptionDetails.builder()
                    .status(statusCode.value())
                    .title(statusCode.toString())
                    .timestamp(LocalDateTime.now())
                    .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                    .build();
        } else if (body instanceof String) {
            body = ExceptionDetails.builder()
                    .status(statusCode.value())
                    .title(statusCode.toString())
                    .timestamp(LocalDateTime.now())
                    .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    private ExceptionDetails.ExceptionDetailsBuilder createExceptionBuilder(HttpStatusCode status, ExceptionType type,
                                                                             String detail){
        return ExceptionDetails.builder()
                .status(status.value())
                .type(type.getUri())
                .title(type.getTitle())
                .detail(detail)
                .timestamp(LocalDateTime.now());
    }

    private String joinPath(List<Reference> references){
        return references.stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));
    }

}
