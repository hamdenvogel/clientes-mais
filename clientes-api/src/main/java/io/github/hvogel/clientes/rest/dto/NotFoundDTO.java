package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;

public class NotFoundDTO {	
	private String url = "http://hvogel.com.br/img/notfound.jpg";

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public int hashCode() {
		return Objects.hash(url);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotFoundDTO other = (NotFoundDTO) obj;
		return Objects.equals(url, other.url);
	}

	@Override
	public String toString() {
		return "NotFoundDTO [url=" + url + "]";
	}
		
}
