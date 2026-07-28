package io.github.hvogel.clientes.model.entity;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class Captcha {
	private String site;
    private String secret;
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	@Override
	public int hashCode() {
		return Objects.hash(secret, site);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Captcha other = (Captcha) obj;
		return Objects.equals(secret, other.secret) && Objects.equals(site, other.site);
	}
	@Override
	public String toString() {
		return "Captcha [site=" + site + ", secret=" + secret + "]";
	}
       
}
