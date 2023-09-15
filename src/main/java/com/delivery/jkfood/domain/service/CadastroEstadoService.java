package com.delivery.jkfood.domain.service;

import com.delivery.jkfood.domain.exception.EntidadeEmUsoException;
import com.delivery.jkfood.domain.exception.EntidadeNaoEncontradaException;
import com.delivery.jkfood.domain.model.Estado;
import com.delivery.jkfood.domain.repository.EstadoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroEstadoService {

    @Autowired
    private EstadoRespository repository;

    public Estado salvar(Estado estado){
        return repository.save(estado);
    }

    public void excluir(Long id){
        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe cadastro de estado com o código %d", id));
        }catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format("O estado com o código %d não pode ser removido, pois está em uso", id));
        }
    }
}
