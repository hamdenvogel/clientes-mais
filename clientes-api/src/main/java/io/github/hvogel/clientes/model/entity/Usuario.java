package io.github.hvogel.clientes.model.entity;

import java.util.HashSet;
import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario", schema = "meusservicos", uniqueConstraints = {
    @UniqueConstraint(columnNames = "login"),
  @UniqueConstraint(columnNames = "email"),
  @UniqueConstraint(columnNames = "cpf")
})
public class Usuario {

  public Usuario(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, name = "login")
  @NotEmpty(message = "{campo.login.obrigatorio}")
  @Size(max = 100)
  private String username;

  @Size(max = 150)
  @Email
  private String email;

  @Column(name = "senha")
  @NotEmpty(message = "{campo.senha.obrigatorio}")
  @Size(max = 120)
  private String password;

  @Column(name = "cpf", nullable = false, unique = true)
  @NotEmpty(message = "CPF e obrigatorio.")
  @Size(max = 14)
  private String cpf;

  @Column(name = "telefone")
  @Size(max = 20)
  private String telefone;

  @Column(name = "endereco")
  @Size(max = 250)
  private String endereco;

  @Column(name = "cidade")
  @Size(max = 100)
  private String cidade;

  @Column(name = "uf")
  @Size(max = 2)
  private String uf;

  @Column(name = "cep")
  @Size(max = 8)
  private String cep;

  @Builder.Default
  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;

  @Builder.Default
  @Column(name = "email_confirmado", nullable = false)
  private boolean emailConfirmed = false;

  @Column(name = "email_confirmado_em")
  private LocalDateTime emailConfirmedAt;

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set<Perfil> getRoles() {
    return roles;
  }

  public void setRoles(Set<Perfil> roles) {
    this.roles = roles;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Builder.Default
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "usuario_perfil", schema = "meusservicos", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "perfil_id"))
  private Set<Perfil> roles = new HashSet<>();
}
