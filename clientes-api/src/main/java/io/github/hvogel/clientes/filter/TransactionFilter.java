package io.github.hvogel.clientes.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.github.hvogel.clientes.util.HttpServletReqUtil;
import io.github.hvogel.clientes.util.MyHttpServletRequestWrapper;

import lombok.RequiredArgsConstructor;

@Component
@Order(1)
@RequiredArgsConstructor
public class TransactionFilter implements Filter {

	private final HttpServletReqUtil reqUtil;
	final Logger logger = LoggerFactory.getLogger(TransactionFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final MyHttpServletRequestWrapper wrappedRequest = new MyHttpServletRequestWrapper(
				(HttpServletRequest) request);
		logger.info("Inside Servlet Filter");
		logger.info("User IP address: {}", reqUtil.getRemoteAddress(wrappedRequest));
		logger.info("Request Params: {}", reqUtil.getRequestParams(wrappedRequest));
		logger.info("Request Payload: {}", reqUtil.getPayLoad(wrappedRequest));
		logger.info("Exiting Servlet Filter");
		chain.doFilter(wrappedRequest, response);
	}
}
