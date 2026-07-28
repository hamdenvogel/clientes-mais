package io.github.hvogel.clientes.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

import io.github.hvogel.clientes.security.jwt.AuthEntryPointJwt;
import io.github.hvogel.clientes.security.jwt.AuthTokenFilter;
import io.github.hvogel.clientes.service.impl.UserDetailsServiceImpl;

import io.github.hvogel.clientes.security.jwt.JwtUtils;
import jakarta.servlet.DispatcherType;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final UserDetailsServiceImpl userDetailsService;
  private final AuthEntryPointJwt unauthorizedHandler;
  private final JwtUtils jwtUtils;

  @Value("${app.cors.allowed-origin-patterns:http://localhost:4200,http://127.0.0.1:4200,http://localhost:*,http://127.0.0.1:*}")
  private List<String> allowedOriginPatterns;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter(jwtUtils, userDetailsService);
  }

  /**
   * Impede que o Spring Boot registre o AuthTokenFilter como filtro de servlet
   * fora da security chain. Sem isso, o filtro roda duas vezes: uma como @Bean
   * (antes da SecurityContextHolderFilter recriar o contexto vazio) e outra
   * dentro da security chain — causando 401 pois o OncePerRequestFilter pula a
   * segunda execução com o contexto já limpo.
   */
  @Bean
  public FilterRegistrationBean<AuthTokenFilter> authTokenFilterRegistration() {
    FilterRegistrationBean<AuthTokenFilter> registration = new FilterRegistrationBean<>(authenticationJwtTokenFilter());
    registration.setEnabled(false);
    return registration;
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(allowedOriginPatterns);
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setExposedHeaders(
        Arrays.asList("Authorization", "Content-Disposition", "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // Permite o fluxo de erro para não mascarar exceções reais como 401 em /error.
            .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/error", "/error/**").permitAll()
            .requestMatchers("/").permitAll()
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/info/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/doc/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/grafico/grafico-status-atendimento-por-periodo").permitAll()
            // Observabilidade local/CI (Prometheus scrape + healthchecks Docker/K8s)
            .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
            .requestMatchers("/actuator/info").permitAll()
            .requestMatchers("/actuator/prometheus").permitAll()
            // Lab mensageria (item 2) — testes locais sem JWT
            .requestMatchers("/api/pedidos/async", "/api/pedidos/async/**").permitAll()
            .requestMatchers("/api/clientes/**", "/api/servicos-prestados/**", "/api/prestador/**", "/api/pacote/**")
            .authenticated()
            .anyRequest().authenticated());

    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
