package com.delivery.jkfood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.delivery.jkfood.domain.model.Estado;

@Repository
public interface EstadoRespository extends JpaRepository<Estado, Long>{
    
}
