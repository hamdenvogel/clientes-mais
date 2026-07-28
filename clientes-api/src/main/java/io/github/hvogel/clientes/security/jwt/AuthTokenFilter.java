package io.github.hvogel.clientes.security.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;

  private final UserDetailsServiceImpl userDetailsService;

  private static final Logger LOG = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      LOG.info("Processing request for URI: {}", request.getRequestURI());
      if (jwt != null) {
        LOG.info("JWT found in header");
        if (jwtUtils.validarToken(jwt)) {
          LOG.info("JWT is valid");
          String username = jwtUtils.getUserNameFromJwtToken(jwt);

          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authentication);
          LOG.info("User {} authenticated with authorities: {}", username, userDetails.getAuthorities());
        } else {
          LOG.warn("JWT is invalid for URI: {}", request.getRequestURI());
        }
      } else {
        LOG.warn("No JWT found in header for URI: {}", request.getRequestURI());
      }
    } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
      LOG.error("Cannot set user authentication (JWT error): {}", e.getMessage());
    } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
      LOG.error("Cannot set user authentication (user not found): {}", e.getMessage());
    } catch (Exception e) {
      LOG.error("Cannot set user authentication (unexpected error): {} - {}", e.getClass().getSimpleName(), e.getMessage());
    }

    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7, headerAuth.length());
    }

    return null;
  }
}
