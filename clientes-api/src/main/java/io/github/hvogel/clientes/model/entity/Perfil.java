package io.github.hvogel.clientes.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import io.github.hvogel.clientes.enums.EPerfil;

@Entity
@Table(name = "perfil", schema = "meusservicos")
public class Perfil {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private EPerfil nome;

  public Perfil() {

  }

  public Perfil(EPerfil nome) {
    this.nome = nome;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public EPerfil getNome() {
    return nome;
  }

  public void setNome(EPerfil nome) {
    this.nome = nome;
  }
}
