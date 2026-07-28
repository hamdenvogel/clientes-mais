package io.github.hvogel.clientes.rest.dto;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
		"success",
		"challenge_ts",
		"hostname",
		"error-codes"
})
public class GoogleRecaptchaDTO {

	@JsonProperty("success")
	private boolean success;

	@JsonProperty("challenge_ts")
	private String challengeTs;

	@JsonProperty("hostname")
	private String hostname;

	@JsonProperty("error-codes")
	private ErrorCode[] errorCodes;

	@JsonIgnore
	public boolean hasClientError() {
		ErrorCode[] errors = getErrorCodes();
		if (errors == null) {
			return false;
		}
		for (ErrorCode error : errors) {
			if (error == ErrorCode.INVALID_RESPONSE || error == ErrorCode.MISSING_RESPONSE
					|| error == ErrorCode.TIMEOUT_OR_DUPLICATE) {
				return true;
			}
		}
		return false;
	}

	public enum ErrorCode {
		MISSING_SECRET,
		INVALID_SECRET,
		MISSING_RESPONSE,
		INVALID_RESPONSE,
		TIMEOUT_OR_DUPLICATE;

		private static Map<String, ErrorCode> errorsMap = new HashMap<>(4);

		static {
			errorsMap.put("missing-input-secret", MISSING_SECRET);
			errorsMap.put("invalid-input-secret", INVALID_SECRET);
			errorsMap.put("missing-input-response", MISSING_RESPONSE);
			errorsMap.put("invalid-input-response", INVALID_RESPONSE);
			errorsMap.put("timeout-or-duplicate", TIMEOUT_OR_DUPLICATE);
		}

		@JsonCreator
		public static ErrorCode forValue(String value) {
			return errorsMap.get(value.toLowerCase());
		}

	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getChallengeTs() {
		return challengeTs;
	}

	public void setChallengeTs(String challengeTs) {
		this.challengeTs = challengeTs;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public ErrorCode[] getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(ErrorCode[] errorCodes) {
		this.errorCodes = errorCodes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(errorCodes);
		result = prime * result + Objects.hash(challengeTs, hostname, success);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoogleRecaptchaDTO other = (GoogleRecaptchaDTO) obj;
		return Objects.equals(challengeTs, other.challengeTs) && Arrays.equals(errorCodes, other.errorCodes)
				&& Objects.equals(hostname, other.hostname) && success == other.success;
	}

	@Override
	public String toString() {
		return "GoogleRecaptchaDTO [success=" + success + ", challengeTs=" + challengeTs + ", hostname=" + hostname
				+ ", errorCodes=" + Arrays.toString(errorCodes) + "]";
	}

}
