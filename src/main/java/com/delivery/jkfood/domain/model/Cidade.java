package com.delivery.jkfood.domain.model;

import com.delivery.jkfood.Groups;
import jakarta.persistence.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cidade {
    
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @Valid
    @NotNull
    @ConvertGroup(from = Default.class, to = Groups.EstadoId.class)
    @ManyToOne
    @JoinColumn(nullable = false)
    private Estado estado;
    
}
