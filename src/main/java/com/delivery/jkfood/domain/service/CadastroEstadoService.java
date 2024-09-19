package com.delivery.jkfood.domain.service;

import com.delivery.jkfood.domain.exception.CozinhaNaoEncontradoException;
import com.delivery.jkfood.domain.exception.EntidadeEmUsoException;
import com.delivery.jkfood.domain.exception.EntidadeNaoEncontradaException;
import com.delivery.jkfood.domain.exception.EstadoNaoEncontradoException;
import com.delivery.jkfood.domain.model.Cozinha;
import com.delivery.jkfood.domain.model.Estado;
import com.delivery.jkfood.domain.repository.EstadoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CadastroEstadoService {

    public static final String MSG_ESTADO_EM_USO = "O estado com o código %d não pode ser removido, pois está em uso";

    @Autowired
    private EstadoRespository repository;

    public Estado buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new CozinhaNaoEncontradoException(id));
    }

    public Estado salvar(Estado estado){
        return repository.save(estado);
    }

    public void excluir(Long id){
        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new CozinhaNaoEncontradoException(id);
        }catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    HttpStatus.CONFLICT, String.format(MSG_ESTADO_EM_USO, id));
        }
    }
}
