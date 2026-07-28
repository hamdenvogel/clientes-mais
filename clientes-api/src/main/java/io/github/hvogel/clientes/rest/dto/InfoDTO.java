package io.github.hvogel.clientes.rest.dto;

import java.util.Objects;
import jakarta.annotation.Generated;

public class InfoDTO {
	private String nameApp;
	private String versionApp;
	private String authorApp;

	public InfoDTO() {
	}

	@Generated("SparkTools")
	private InfoDTO(Builder builder) {
		this.nameApp = builder.nameApp;
		this.versionApp = builder.versionApp;
		this.authorApp = builder.authorApp;
	}

	public String getNameApp() {
		return nameApp;
	}

	public void setNameApp(String nameApp) {
		this.nameApp = nameApp;
	}

	public String getVersionApp() {
		return versionApp;
	}

	public void setVersionApp(String versionApp) {
		this.versionApp = versionApp;
	}

	public String getAuthorApp() {
		return authorApp;
	}

	public void setAuthorApp(String authorApp) {
		this.authorApp = authorApp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(authorApp, nameApp, versionApp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InfoDTO other = (InfoDTO) obj;
		return Objects.equals(authorApp, other.authorApp) && Objects.equals(nameApp, other.nameApp)
				&& Objects.equals(versionApp, other.versionApp);
	}

	@Override
	public String toString() {
		return "InfoDTO [nameApp=" + nameApp + ", versionApp=" + versionApp + ", authorApp=" + authorApp + "]";
	}

	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	@Generated("SparkTools")
	public static final class Builder {
		private String nameApp;
		private String versionApp;
		private String authorApp;

		private Builder() {
		}

		public Builder withNameApp(String nameApp) {
			this.nameApp = nameApp;
			return this;
		}

		public Builder withVersionApp(String versionApp) {
			this.versionApp = versionApp;
			return this;
		}

		public Builder withAuthorApp(String authorApp) {
			this.authorApp = authorApp;
			return this;
		}

		public InfoDTO build() {
			return new InfoDTO(this);
		}
	}

}
