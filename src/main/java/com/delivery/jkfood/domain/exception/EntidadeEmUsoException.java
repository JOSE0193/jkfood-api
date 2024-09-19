package com.delivery.jkfood.domain.exception;

import org.springframework.http.HttpStatus;

public class EntidadeEmUsoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntidadeEmUsoException(HttpStatus conflict, String mensagem) {
        super(mensagem);
    }
}
