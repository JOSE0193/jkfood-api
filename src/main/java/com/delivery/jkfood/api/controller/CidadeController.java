package com.delivery.jkfood.api.controller;

import com.delivery.jkfood.domain.exception.EntidadeEmUsoException;
import com.delivery.jkfood.domain.exception.EntidadeNaoEncontradaException;
import com.delivery.jkfood.domain.exception.EstadoNaoEncontradoException;
import com.delivery.jkfood.domain.exception.NegocioException;
import com.delivery.jkfood.domain.model.Cidade;
import com.delivery.jkfood.domain.model.Cozinha;
import com.delivery.jkfood.domain.repository.CidadeRepository;
import com.delivery.jkfood.domain.service.CadastroCidadeService;
import com.sun.source.tree.OpensTree;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private CidadeRepository repository;
    @Autowired
    private CadastroCidadeService service;

    @GetMapping
    public @ResponseBody List<Cidade> listar(){
        return  repository.findAll();
    }

    @GetMapping("/{id}")
    public Cidade buscar(@PathVariable Long id){ return service.buscar(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cidade salvar(@RequestBody Cidade cidade){
        try {
            return  service.salvar(cidade);
        } catch (EstadoNaoEncontradoException e) {
        throw new NegocioException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public Cidade atualizar(@PathVariable Long id, @RequestBody Cidade cidade) {
        try {
            Cidade cidadeAtual = service.buscar(id);
            BeanUtils.copyProperties(cidade, cidadeAtual, "id");
            return service.salvar(cidadeAtual);
        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) { service.excluir(id);  }

}
