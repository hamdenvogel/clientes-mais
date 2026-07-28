package io.github.hvogel.clientes.service;

public interface ReCaptchaAttemptService {
	void reCaptchaSucceeded(final String key);
	void reCaptchaFailed(final String key);
	boolean isBlocked(final String key);
	void reCaptchaDeleteAllEntriesInTheCache();
	String reCapthaSizeOfCacheVersusTotalAttempts();
}
