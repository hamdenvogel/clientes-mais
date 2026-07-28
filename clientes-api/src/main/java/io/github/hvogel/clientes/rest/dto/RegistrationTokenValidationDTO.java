package io.github.hvogel.clientes.rest.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationTokenValidationDTO {
  private boolean valid;
  private String message;
  private LocalDateTime expiresAt;
}

