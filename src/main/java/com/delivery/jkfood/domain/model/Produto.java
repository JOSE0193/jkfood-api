package com.delivery.jkfood.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Produto {
	
    @EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private String descricao;

	@Column(nullable = false)
	private BigDecimal preco;

	@Column(nullable = false)
	private Boolean ativo;

	@ManyToOne
	@JoinColumn(name = "restaurante_id", nullable = false)
	private Restaurante restaurante;

}
