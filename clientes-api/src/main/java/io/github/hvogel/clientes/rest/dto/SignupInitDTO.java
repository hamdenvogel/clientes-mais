package io.github.hvogel.clientes.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupInitDTO {

  @NotBlank(message = "O login e obrigatorio.")
  @Size(min = 3, max = 20, message = "O login deve conter entre 3 e 20 caracteres.")
  private String username;

  @NotBlank(message = "O e-mail e obrigatorio.")
  @Email(message = "Informe um e-mail valido.")
  @Size(max = 150, message = "O e-mail deve conter no maximo 150 caracteres.")
  private String email;
}

