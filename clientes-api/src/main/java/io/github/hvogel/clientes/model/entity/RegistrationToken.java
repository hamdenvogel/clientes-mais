package io.github.hvogel.clientes.model.entity;

import java.time.LocalDateTime;

import io.github.hvogel.clientes.enums.RegistrationTokenPurpose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "registration_token", schema = "meusservicos")
public class RegistrationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "usuario_id", nullable = false)
  private Usuario usuario;

  @Column(name = "token_hash", nullable = false, length = 128)
  private String tokenHash;

  @Enumerated(EnumType.STRING)
  @Column(name = "purpose", nullable = false, length = 40)
  private RegistrationTokenPurpose purpose;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Column(name = "used_at")
  private LocalDateTime usedAt;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}

