package io.github.hvogel.clientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RestController
public class SistemaDeClientesApplication {

	private final Environment env;

	public SistemaDeClientesApplication(Environment env) {
		this.env = env;
	}

	@GetMapping("/")
	public String getAmbiente() {
		String ambienteAtual = "DES";

		if (env.getActiveProfiles().length > 0) {
			ambienteAtual = env.getActiveProfiles()[0].toUpperCase();
		}

		String appName = env.getProperty("application.env");
		return "Ambiente: %s | versao: %s".formatted(ambienteAtual, appName);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			System.out.println("###################################################");
			System.out.println("  SISTEMA DE CLIENTES STARTED SUCCESSFULLY        ");
			System.out.println("  Active Profiles: " + String.join(", ", env.getActiveProfiles()));
			System.out.println("  Server Port: " + env.getProperty("server.port"));
			System.out.println("###################################################");
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeClientesApplication.class, args);
	}
}
