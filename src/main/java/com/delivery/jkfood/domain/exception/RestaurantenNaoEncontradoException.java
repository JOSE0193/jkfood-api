package com.delivery.jkfood.domain.exception;

public class RestaurantenNaoEncontradoException extends EntidadeNaoEncontradaException {

    private static final long serialVersionUID = 1L;

    public RestaurantenNaoEncontradoException(String mensagem){
        super(mensagem);
    }

    public RestaurantenNaoEncontradoException(Long restauranteId) {
        this(String.format("Não existe cadastro de restaurante com o código %d", restauranteId));
    }

}
