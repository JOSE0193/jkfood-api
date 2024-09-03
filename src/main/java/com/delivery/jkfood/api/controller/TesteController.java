package com.delivery.jkfood.api.controller;

import com.delivery.jkfood.domain.model.Cozinha;
import com.delivery.jkfood.domain.model.Restaurante;
import com.delivery.jkfood.domain.repository.CozinhaRepository;
import com.delivery.jkfood.domain.repository.RestauranteRepository;
import com.delivery.jkfood.infrastructure.repository.spec.RestauranteSpecs;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/teste")
public class TesteController {

    @Autowired
    private RestauranteRepository restauranteRepository;
    private CozinhaRepository cozinhaRepository;

    @GetMapping("/cozinhas/por-nome")
    public List<Cozinha> cozinhasPorNome(String nome){
        return cozinhaRepository.findByNomeContaining(nome);
    }

    @GetMapping("/restaurantes/por-nome-cozinha")
    public List<Restaurante> restaurantesPorNome(String nome, Long cozinhaId){
        return restauranteRepository.consultaPorNomeECozinha(nome, cozinhaId);
    }

    @GetMapping("/restaurantes/por-primeiro-nome")
    public Optional<Restaurante> restaurantePrimeiro(String nome){
        return restauranteRepository.findFirstRestauranteByNomeContaining(nome);
    }

    @GetMapping("/restaurantes/por-taxa-frete")
    public List<Restaurante> restaurantesPorTaxaFrete(String nome, BigDecimal taxaInicial, BigDecimal taxaFinal){
        return restauranteRepository.find(nome, taxaInicial, taxaFinal);
    }

    @GetMapping("/restaurantes/frete-gratis")
    public List<Restaurante> restaurantesFreteGratis(String nome) {
        return restauranteRepository.findFreteGratis(nome);
    }

    @GetMapping("/restaurantes/por-cozinha")
    public Integer quantidadePorCozinha(Long cozinhaId) {
        return (Integer) restauranteRepository.countByCozinhaId(cozinhaId);
    }

    @GetMapping("/restaurantes/primeiro")
    public Optional<Restaurante> restaurantePrimeiro() {
        return restauranteRepository.buscarPrimeiro();
    }

}
