package io.github.hvogel.clientes.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Scanner;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class HttpServletReqUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpServletReqUtil.class);

	public String getRemoteAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	public String getPayLoad(HttpServletRequest request) {

		final String method = request.getMethod().toUpperCase();

		if ("POST".equals(method) || "PUT".equals(method)) {
			return extractRequestBody(request);
		}
		return "Not a POST or PUT method";
	}

	public String getRequestParams(HttpServletRequest request) {
		final StringBuilder params = new StringBuilder();

		Enumeration<String> parameterNames = request.getParameterNames();

		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			String paramValue = request.getParameter(paramName);
			if ("password".equalsIgnoreCase(paramName) || "pwd".equalsIgnoreCase(paramName)) {
				paramValue = "*****";
			}
			params.append(paramName).append(": ").append(paramValue).append(System.lineSeparator());
		}
		return params.toString();

	}

	private static String extractRequestBody(HttpServletRequest request) {
		String method = request.getMethod();
		if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
			try (Scanner s = new Scanner(request.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A")) {
				String payload = s.hasNext() ? s.next() : "";
				return maskSensitiveData(payload);
			} catch (IOException e) {
				logger.error("Error extracting request body", e);
				return "";
			}
		}
		return "";
	}

	protected static String maskSensitiveData(String content) {
		if (content == null || content.isEmpty()) {
			return "";
		}
		// Regex to identify JSON fields: "key" : "value" or "key":"value"
		// Groups:
		// 1: Key (password, senha, etc)
		// 2: Separator and quotes
		// 3: Value to handle
		return content.replaceAll("(?i)(\"(?:password|senha|token|access_token|accessToken)\"\\s*:\\s*\\\")([^\\\"]+)(\\\")",
				"$1*****$3");
	}
}
