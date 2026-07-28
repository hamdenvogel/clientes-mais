package io.github.hvogel.clientes.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.hvogel.clientes.rest.dto.InfoDTO;
import io.github.hvogel.clientes.service.InfoService;

@Service
public class InfoServiceImpl implements InfoService {
	
	@Value("${application.name}")
	private String appName;
	
	@Value("${application.version}")
	private String appVersion;
	
	@Value("${application.author}")
	private String appAuthor;

	@Override
	public InfoDTO obterInformacoesAplicacao() {
		return InfoDTO.builder()
				.withNameApp(appName)
				.withAuthorApp(appAuthor)
				.withVersionApp(appVersion)
				.build();
	}
}
