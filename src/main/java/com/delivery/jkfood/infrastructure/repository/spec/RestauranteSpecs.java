package com.delivery.jkfood.infrastructure.repository.spec;

import com.delivery.jkfood.domain.model.Restaurante;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class RestauranteSpecs {

    public static Specification<Restaurante> freteGratis() {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("taxaFrete"), BigDecimal.ZERO));
    }

    public static Specification<Restaurante> nomeSemelhante(String nome) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("nome"), "%" + nome +"%"));
    }
}
