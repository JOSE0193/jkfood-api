package com.delivery.jkfood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.math.BigDecimal;

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

	@Column
	private String descricao;

	@Column
	private BigDecimal preco;

	@Column
	private Boolean ativo;

	@ManyToOne
	@JoinColumn(name = "restaurante_id")
	private Restaurante restaurante;
	
}
