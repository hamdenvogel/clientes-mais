package io.github.hvogel.clientes.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.message.BasicHeaderElementIterator;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class HttpClientConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientConfig.class);

	//@formatter:off
	private static final int CONNECT_TIMEOUT = Integer.getInteger("HTTP_CONNECT_TIMEOUT", 10_000);
	private static final int REQUEST_TIMEOUT = Integer.getInteger("HTTP_REQUEST_TIMEOUT", 30_000);
	private static final int SOCKET_TIMEOUT = Integer.getInteger("HTTP_REQUEST_TIMEOUT", REQUEST_TIMEOUT);
	private static final int MAX_TOTAL_CONNECTIONS = Integer.getInteger("MAX_TOTAL_CONNECTIONS", 50);
	private static final int DEFAULT_KEEP_ALIVE_TIME_MILLIS 
				= Integer.getInteger("DEFAULT_KEEP_ALIVE_TIME_MILLIS", 20_000);
	private static final int CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS 
				= Integer.getInteger("DEFAULT_KEEP_ALIVE_TIME_MILLIS", DEFAULT_KEEP_ALIVE_TIME_MILLIS);
	//@formatter:on

	@Bean
	public PoolingHttpClientConnectionManager poolingConnectionManager() {
		SSLContextBuilder builder = new SSLContextBuilder();
		try {
			builder.loadTrustMaterial(null, (chain, authType) -> true); // Trust all certificates
		} catch (NoSuchAlgorithmException | KeyStoreException e) {
			LOGGER.error("Pooling Connection Manager Initialisation failure because of "
					+ e.getMessage(), e);
		}

		SSLConnectionSocketFactory sslsf = null;
		try {
			sslsf = new SSLConnectionSocketFactory(builder.build());
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			LOGGER.error("Pooling Connection Manager Initialisation failure because of "
					+ e.getMessage(), e);
		}

		org.apache.hc.core5.http.config.Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
				.<ConnectionSocketFactory>create().register("https", sslsf)
				.register("http", new PlainConnectionSocketFactory())
				.build();

		PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager(
				socketFactoryRegistry,
				PoolConcurrencyPolicy.STRICT,
				PoolReusePolicy.LIFO,
				TimeValue.ofMilliseconds(DEFAULT_KEEP_ALIVE_TIME_MILLIS));
		poolingConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);

		ConnectionConfig connectionConfig = ConnectionConfig.custom()
				.setConnectTimeout(Timeout.ofMilliseconds(CONNECT_TIMEOUT))
				.build();
		poolingConnectionManager.setDefaultConnectionConfig(connectionConfig);

		return poolingConnectionManager;
	}

	@Bean
	public CloseableHttpClient httpClient() {
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(Timeout.ofMilliseconds(REQUEST_TIMEOUT))
				.setResponseTimeout(Timeout.ofMilliseconds(SOCKET_TIMEOUT))
				.build();

		return HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(poolingConnectionManager())
				.setKeepAliveStrategy((response, context) -> {
					BasicHeaderElementIterator it = new BasicHeaderElementIterator(
							response.headerIterator(HttpHeaders.KEEP_ALIVE));
					while (it.hasNext()) {
						HeaderElement he = it.next();
						String param = he.getName();
						String value = he.getValue();
						if (value != null && param.equalsIgnoreCase("timeout")) {
							return TimeValue.ofSeconds(Long.parseLong(value));
						}
					}
					return TimeValue.ofMilliseconds(DEFAULT_KEEP_ALIVE_TIME_MILLIS);
				})
				.build();
	}

	@Bean
	public Runnable idleConnectionMonitor(final PoolingHttpClientConnectionManager connectionManager) {
		return new Runnable() {

			@Override
			@Scheduled(fixedDelay = 10000)
			public void run() {
				if (connectionManager != null) {
					LOGGER.trace(
							"run IdleConnectionMonitor - Closing expired and idle connections...");
					connectionManager.closeExpired();
					connectionManager.closeIdle(TimeValue.ofSeconds(CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS));
				} else {
					LOGGER.trace(
							"run IdleConnectionMonitor - Http Client Connection manager is not initialised");
				}
			}
		};
	}
}
