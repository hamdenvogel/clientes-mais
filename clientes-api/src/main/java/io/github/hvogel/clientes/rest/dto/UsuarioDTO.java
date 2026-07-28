package io.github.hvogel.clientes.rest.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "Login e obrigatorio.")
    @Size(max = 100)
    private String username;

    @Email(message = "Email invalido.")
    @Size(max = 150)
    private String email;

    @Size(max = 120)
    private String password;

    @NotBlank(message = "CPF e obrigatorio.")
    @Size(max = 14)
    private String cpf;

    @Size(max = 20)
    private String telefone;

    @Size(max = 250)
    private String endereco;

    @Size(max = 100)
    private String cidade;

    @Size(max = 2)
    private String uf;

    @Size(max = 8)
    private String cep;

    private boolean ativo = true;

    private boolean emailConfirmed;

    private List<String> roles = new ArrayList<>();

    private InfoResponseDTO infoResponseDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public InfoResponseDTO getInfoResponseDTO() {
        return infoResponseDTO;
    }

    public void setInfoResponseDTO(InfoResponseDTO infoResponseDTO) {
        this.infoResponseDTO = infoResponseDTO;
    }
}
