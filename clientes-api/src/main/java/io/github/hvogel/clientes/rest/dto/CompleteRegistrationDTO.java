package io.github.hvogel.clientes.rest.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompleteRegistrationDTO {

  @NotBlank(message = "Token de confirmacao obrigatorio.")
  private String token;

  @NotBlank(message = "A senha e obrigatoria.")
  @Size(min = 8, max = 60, message = "A senha deve conter entre 8 e 60 caracteres.")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s])\\S+$",
      message = "A senha deve conter ao menos uma letra maiuscula, uma minuscula, um numero e um caractere especial, sem espacos.")
  private String password;

  @NotBlank(message = "A confirmacao da senha e obrigatoria.")
  private String confirmPassword;

  @AssertTrue(message = "As senhas informadas nao conferem.")
  public boolean isPasswordConfirmationValid() {
    return password != null && password.equals(confirmPassword);
  }
}

