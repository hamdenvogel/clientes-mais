package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;

public class CredenciaisDTO {
    private String login;
    private String senha;
    
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	@Override
	public int hashCode() {
		return Objects.hash(login, senha);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CredenciaisDTO other = (CredenciaisDTO) obj;
		return Objects.equals(login, other.login) && Objects.equals(senha, other.senha);
	}
	@Override
	public String toString() {
		return "CredenciaisDTO [login=" + login + ", senha=" + senha + "]";
	}  
	    
}
