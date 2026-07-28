package io.github.hvogel.clientes.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "produto" , schema = "meusservicos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "descricao")
    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String descricao;

    @Column(name = "preco_unitario")
    @NotNull(message = "{campo.preco.obrigatorio}")
    private BigDecimal preco;
    
    @Column(name = "marca", length = 50)
    private String marca;
    
    @Column(name = "modelo", length = 50)
    private String modelo;
    
    @Column(name = "anoFabricacao")
    private Integer anoFabricacao;
    
    @Column(name = "anoModelo")
    private Integer anoModelo;
}
