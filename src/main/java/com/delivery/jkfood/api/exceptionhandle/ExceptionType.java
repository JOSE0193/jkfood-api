package com.delivery.jkfood.api.exceptionhandle;

import lombok.Getter;

@Getter
public enum ExceptionType {

    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem Incompreensível"),
    ENTIDADE_NAO_ENCONTRADA("/entidade-nao-encontrada", "Entidade não encontrada"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade está em uso"),
    ERRO_NEGOCIO("/erro-negocio", "Erro na regra de negócio");

    String uri;
    String title;

    ExceptionType(String path, String title) {
        this.uri = "https://jkfood.com.br" + path;
        this.title = title;
    }
}
