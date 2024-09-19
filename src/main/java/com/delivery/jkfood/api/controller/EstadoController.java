package com.delivery.jkfood.api.controller;

import java.util.List;
import java.util.Optional;

import com.delivery.jkfood.domain.exception.EntidadeEmUsoException;
import com.delivery.jkfood.domain.exception.EntidadeNaoEncontradaException;
import com.delivery.jkfood.domain.service.CadastroEstadoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.delivery.jkfood.domain.model.Estado;
import com.delivery.jkfood.domain.repository.EstadoRespository;

@RestController
@RequestMapping("/estados")
public class EstadoController {
    
    @Autowired
    private EstadoRespository repository;
    @Autowired
    private CadastroEstadoService service;
    
    @GetMapping
    public List<Estado> listar(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Estado buscar(@PathVariable Long id){
        return service.buscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estado adicionar(@RequestBody Estado estado){
        return service.salvar(estado);
    }

    @PutMapping("/{id}")
    public Estado atualizar(@PathVariable Long id, @RequestBody Estado estado){
        Estado estadoAtual = service.buscar(id);
        BeanUtils.copyProperties(estado, estadoAtual, "id");
        estado = service.salvar(estadoAtual);

        return estado;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir (@PathVariable Long id){
         service.excluir(id);
    }
}
