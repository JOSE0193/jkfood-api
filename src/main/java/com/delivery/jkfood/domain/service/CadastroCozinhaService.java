package com.delivery.jkfood.domain.service;

import com.delivery.jkfood.domain.exception.CozinhaNaoEncontradoException;
import com.delivery.jkfood.domain.exception.EntidadeEmUsoException;
import com.delivery.jkfood.domain.exception.EntidadeNaoEncontradaException;
import com.delivery.jkfood.domain.model.Cozinha;
import com.delivery.jkfood.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CadastroCozinhaService {

    public static final String MSG_ENTIDADE_EM_USO = "Cozinha de código %d não pode ser removida, pois está em uso";

    @Autowired
    private CozinhaRepository repository;

    public Cozinha buscar(Long id) {
       return repository.findById(id).orElseThrow(() -> new CozinhaNaoEncontradoException(id));
    }

    public Cozinha salvar(Cozinha cozinha){
        return repository.save(cozinha);
    }

    public void excluir (Long id){
        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e) {
            throw new CozinhaNaoEncontradoException(id);
        } catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(
                    HttpStatus.CONFLICT, String.format(MSG_ENTIDADE_EM_USO, id)
            );
        }
    }

}
