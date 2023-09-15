package com.delivery.jkfood.domain.repository;

import com.delivery.jkfood.domain.model.Restaurante;

import java.math.BigDecimal;
import java.util.List;

public interface RestauranteRepositoryQueries {

    public List<Restaurante> find(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal);

    List<Restaurante> findFreteGratis(String nome);

}
