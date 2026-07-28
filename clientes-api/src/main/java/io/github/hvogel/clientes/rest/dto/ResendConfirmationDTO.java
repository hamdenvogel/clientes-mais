package io.github.hvogel.clientes.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResendConfirmationDTO {

  @NotBlank(message = "O e-mail e obrigatorio.")
  @Email(message = "Informe um e-mail valido.")
  @Size(max = 150, message = "O e-mail deve conter no maximo 150 caracteres.")
  private String email;
}

