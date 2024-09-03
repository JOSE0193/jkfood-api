package com.delivery.jkfood.domain.model;

import jakarta.persistence.AttributeConverter;
import lombok.*;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
@ToString
public enum StatusPedido {

    CRIADO("CRIADO"),
    CONFIRMADO("CONFIRMADO"),
    ENTREGUE("ENTREGUE"),
    CANCELADO("CANCELADO");


    private String valor;

    public static StatusPedido get(String valor) {
        return Arrays.stream(values())
                .filter(it -> it.valor.equals(valor))
                .findFirst()
                .orElse(null);
    }

    public static class StatusPedidoConverter implements AttributeConverter<StatusPedido, String> {

        @Override
        public String convertToDatabaseColumn(StatusPedido tipoJulgamento) {
            return Optional.ofNullable(tipoJulgamento)
                    .map(StatusPedido::getValor)
                    .orElse(null);
        }

        @Override
        public StatusPedido convertToEntityAttribute(String valor) {
            return StatusPedido.get(valor);
        }
    }

}
