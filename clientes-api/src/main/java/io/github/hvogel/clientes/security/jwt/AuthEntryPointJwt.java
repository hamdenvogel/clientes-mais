package io.github.hvogel.clientes.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
  private static final String ACCEPT_HEADER = "Accept";
  private final RedirectStrategy redirect = new DefaultRedirectStrategy();

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {

    String authHeader = request.getHeader("Authorization");
    logger.error("401 Unauthorized at path: {}. Auth Header: {}. Exception: {}",
        request.getServletPath(), authHeader, authException.getMessage());

    if (isHtmlRequest(request)) {
      redirect.sendRedirect(request, response, "/");
    } else {
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      logger.error("Unauthorized error: {}", authException.getMessage());

      final Map<String, Object> body = new HashMap<>();
      body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
      body.put("error", "Unauthorized");
      body.put("message", authException.getMessage());
      body.put("path", request.getServletPath());

      final ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(response.getOutputStream(), body);
    }
  }

  private boolean isHtmlRequest(HttpServletRequest request) {
    String acceptHeader = request.getHeader(ACCEPT_HEADER);
    List<MediaType> acceptedMediaTypes = MediaType.parseMediaTypes(acceptHeader);
    return acceptedMediaTypes.contains(MediaType.TEXT_HTML);
  }

}
