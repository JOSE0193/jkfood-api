package com.delivery.jkfood.api.controller;

import com.delivery.jkfood.domain.exception.CozinhaNaoEncontradoException;
import com.delivery.jkfood.domain.exception.EntidadeEmUsoException;
import com.delivery.jkfood.domain.exception.EntidadeNaoEncontradaException;
import com.delivery.jkfood.domain.exception.NegocioException;
import com.delivery.jkfood.domain.model.Restaurante;
import com.delivery.jkfood.domain.repository.RestauranteRepository;
import com.delivery.jkfood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository repository;

    @Autowired
    private CadastroRestauranteService service;

    @GetMapping
    public List<Restaurante> listar(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Restaurante buscar(@PathVariable Long id){ return service.buscar(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurante salvar(@RequestBody Restaurante restaurante){
         try {
             return service.salvar(restaurante);
         } catch (CozinhaNaoEncontradoException e){
             throw new NegocioException(e.getMessage(), e);
         }
    }

    @PutMapping("/{id}")
    public Restaurante atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante ){
        try {
            Restaurante restauranteAtual = service.buscar(id);
            BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento",
                    "endereco", "dataCadastro");
            return service.salvar(restauranteAtual);
        } catch (CozinhaNaoEncontradoException e){
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @PatchMapping("/{id}")
    public Restaurante atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> campos){
        Restaurante restaurante = service.buscar(id);
        merge(campos, restaurante);
        atualizar(id, restaurante);
        return restaurante;
    }

    private void merge(Map<String, Object>dadosOrigem, Restaurante restauranteDestino){
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurante restauranteOrigem  = objectMapper.convertValue(dadosOrigem, Restaurante.class);
        dadosOrigem.forEach((nome, valor) -> {
            Field field = ReflectionUtils.findField(Restaurante.class, nome);
            field.setAccessible(true);
            Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);
            ReflectionUtils.setField(field,restauranteDestino, novoValor);
        });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> remover(@PathVariable Long id){
        try {
            service.excluir(id);
            return ResponseEntity.noContent().build();
        }catch (EntidadeNaoEncontradaException e){
            return ResponseEntity.notFound().build();
        }catch (EntidadeEmUsoException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
