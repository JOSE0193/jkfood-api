package com.delivery.jkfood.domain.service;

import com.delivery.jkfood.domain.exception.CidadeNaoEncontradoException;
import com.delivery.jkfood.domain.exception.EntidadeEmUsoException;
import com.delivery.jkfood.domain.exception.EntidadeNaoEncontradaException;
import com.delivery.jkfood.domain.model.Cidade;
import com.delivery.jkfood.domain.model.Estado;
import com.delivery.jkfood.domain.repository.CidadeRepository;
import com.delivery.jkfood.domain.repository.EstadoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CadastroCidadeService {

    public static final String MSG_CIDADE_NAO_ENCONTRADA = "Não existe um cadastro de cidade com o código %d";
    public static final String MSG_CIDADE_EM_USO = "Cidade de código %d não pode ser removida, pois está em uso";

    @Autowired
    private CidadeRepository repository;
    @Autowired
    private CadastroEstadoService cadastroEstadoService;

    public Cidade buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new CidadeNaoEncontradoException(id));
    }

    public Cidade salvar(Cidade cidade){
        Long estadoId = cidade.getEstado().getId();
        Estado estado = cadastroEstadoService.buscar(estadoId);
        cidade.setEstado(estado);
        return repository.save(cidade);
    }

    public void excluir(Long id){
        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new CidadeNaoEncontradoException(id);
        } catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(
                    HttpStatus.CONFLICT, String.format(MSG_CIDADE_EM_USO, id)
            );
        }
    }
}
