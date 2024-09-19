package com.delivery.jkfood.domain.service;

import com.delivery.jkfood.domain.exception.EntidadeEmUsoException;
import com.delivery.jkfood.domain.exception.EntidadeNaoEncontradaException;
import com.delivery.jkfood.domain.exception.RestaurantenNaoEncontradoException;
import com.delivery.jkfood.domain.model.Cidade;
import com.delivery.jkfood.domain.model.Cozinha;
import com.delivery.jkfood.domain.model.Restaurante;
import com.delivery.jkfood.domain.repository.CozinhaRepository;
import com.delivery.jkfood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CadastroRestauranteService {

    public static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Não existe cadastro de cozinha com o código %d";
    public static final String MSG_RESTAURANTE_EM_USO = "Restaurante de código %d não pode ser excluido, pois está em uso";

    @Autowired
    private RestauranteRepository repository;
    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    public Restaurante buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RestaurantenNaoEncontradoException(id));
    }

    public Restaurante salvar(Restaurante restaurante){
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cadastroCozinhaService.buscar(cozinhaId);
        restaurante.setCozinha(cozinha);
        return repository.save(restaurante);
    }

    public void excluir(Long id){
        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new RestaurantenNaoEncontradoException(id);
        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(
                    HttpStatus.CONFLICT, String.format(MSG_RESTAURANTE_EM_USO, id));
        }
    }
}
