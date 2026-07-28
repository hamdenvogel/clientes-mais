package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;

public class TokenDTO {
    private String login;
    private String accessToken;
    private Integer expiresIn;
    private String tokenType;
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Integer getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	@Override
	public int hashCode() {
		return Objects.hash(accessToken, expiresIn, login, tokenType);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TokenDTO other = (TokenDTO) obj;
		return Objects.equals(accessToken, other.accessToken) && Objects.equals(expiresIn, other.expiresIn)
				&& Objects.equals(login, other.login) && Objects.equals(tokenType, other.tokenType);
	}
	@Override
	public String toString() {
		return "TokenDTO [login=" + login + ", accessToken=" + accessToken + ", expiresIn=" + expiresIn
				+ ", tokenType=" + tokenType + "]";
	}
        
}
