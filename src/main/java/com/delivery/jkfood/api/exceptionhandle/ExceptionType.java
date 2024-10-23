package com.delivery.jkfood.api.exceptionhandle;

import lombok.Getter;

@Getter
public enum ExceptionType {

    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem Incompreensível"),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade está em uso"),
    ERRO_NEGOCIO("/erro-negocio", "Erro na regra de negócio"),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parâmetro inválido"),
    ERRO_DE_SISTEMA("/erro-sistema", "Erro interno no sistema"),
    DADOS_INVALIDOS("/dados-invalidos", "Dados Invalidos");

    String uri;
    String title;

    ExceptionType(String path, String title) {
        this.uri = "https://jkfood.com.br" + path;
        this.title = title;
    }
}
