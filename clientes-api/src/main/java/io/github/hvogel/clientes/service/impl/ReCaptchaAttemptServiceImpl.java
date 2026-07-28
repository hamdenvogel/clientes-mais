package io.github.hvogel.clientes.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.github.hvogel.clientes.service.ReCaptchaAttemptService;

@Service("reCaptchaAttemptService")
public class ReCaptchaAttemptServiceImpl implements ReCaptchaAttemptService {
	private static final int MAX_ATTEMPT = 4;
	private LoadingCache<String, Integer> attemptsCache;
	private Integer contador = 0;

	public ReCaptchaAttemptServiceImpl() {
		super();
		attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(4, TimeUnit.HOURS)
				.build(new CacheLoader<String, Integer>() {
					@Override
					public Integer load(final String key) {
						return 0;
					}
				});
	}

	@Override
	public void reCaptchaSucceeded(String key) {
		attemptsCache.invalidate(key);
		this.contador = 0;
	}

	@Override
	public void reCaptchaFailed(String key) {
		int attempts = attemptsCache.getUnchecked(key);
		attempts++;
		this.contador++;
		attemptsCache.put(key, attempts);
	}

	@Override
	public boolean isBlocked(String key) {
		return attemptsCache.getUnchecked(key) >= MAX_ATTEMPT;
	}

	@Override
	public void reCaptchaDeleteAllEntriesInTheCache() {
		attemptsCache.invalidateAll();
		this.contador = 0;
	}

	@Override
	public String reCapthaSizeOfCacheVersusTotalAttempts() {
		return "Tentativas: %s/%s".formatted(this.contador, MAX_ATTEMPT);
	}

}
